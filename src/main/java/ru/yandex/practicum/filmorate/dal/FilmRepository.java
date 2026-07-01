package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.director.Director;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository
public class FilmRepository extends BaseRepository<Film> {
    private static final String FIND_ALL_QUERY = "SELECT f.id AS film_id, f.name, f.description, f.release_date, f.duration, " +
            "f.mpa_id, m.name AS mpa_name, fg.genre_id, g.name AS genre_name, fd.director_id, d.name AS director_name " +
            "FROM films f " +
            "LEFT JOIN mpa m ON f.mpa_id = m.id " +
            "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
            "LEFT JOIN genres g ON fg.genre_id = g.id " +
            "LEFT JOIN film_directors fd ON f.id = fd.film_id " +
            "LEFT JOIN directors d ON fd.director_id = d.id ";
    private static final String FIND_BY_ID_QUERY = "SELECT f.id AS film_id, f.name, f.description, f.release_date, f.duration, " +
            "f.mpa_id, m.name AS mpa_name, fg.genre_id, g.name AS genre_name, fd.director_id, d.name AS director_name " +
            "FROM films f " +
            "LEFT JOIN mpa m ON f.mpa_id = m.id " +
            "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
            "LEFT JOIN genres g ON fg.genre_id = g.id " +
            "LEFT JOIN film_directors fd ON f.id = fd.film_id " +
            "LEFT JOIN directors d ON fd.director_id = d.id " +
            "WHERE f.id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, mpa_id)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?," +
            " duration = ?, mpa_id = ? WHERE id = ?";
    private static final String FIND_LIKE_QUERY = "SELECT COUNT(*) FROM film_likes WHERE film_id = ? AND user_id = ?";
    private static final String ADD_LIKE_QUERY = "INSERT INTO film_likes(film_id, user_id)" +
            "VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
    private static final String INSERT_GENRES_QUERY = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_GENRES_QUERY = "DELETE FROM film_genres WHERE film_id = ?";
    private static final String DELETE_FILM_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String SEARCH_COMMON_FILMS = FIND_ALL_QUERY +
            " WHERE f.id IN ( " +
            "    SELECT film_id FROM film_likes WHERE user_id = ? " +
            "    INTERSECT " +
            "    SELECT film_id FROM film_likes WHERE user_id = ? ) " +
            " ORDER BY (SELECT COUNT(*) FROM film_likes WHERE film_id = f.id) DESC";


    private static final String POPULAR_FILMS_BASE_QUERY = "SELECT f.id " +
            "FROM films f " +
            "LEFT JOIN film_likes fl ON f.id = fl.film_id ";
    private static final String JOIN_FILM_GENRES_QUERY = "LEFT JOIN film_genres fg ON f.id = fg.film_id ";
    private static final String EXTRACT_YEAR_QUERY = "EXTRACT(YEAR FROM f.release_date) = ? ";
    private static final String GROUP_ORDER_LIMIT_QUERY = "GROUP BY f.id " +
            "ORDER BY COUNT(fl.user_id) DESC, f.id ASC " +
            "LIMIT ?";
    public static final String GET_RECOMMENDATE_FILMS = FIND_ALL_QUERY +
            " LEFT JOIN film_likes fl ON  f.id = fl.film_id " +
            " WHERE f.id IN (:filmIds)" +
            " GROUP BY f.id" +
            " ORDER BY COUNT(fl.user_id);";

    private static final String GET_FILMS_BY_DIRECTOR_SORTED_BY_LIKES =
            "SELECT f.id AS film_id, f.name, f.description, f.release_date, f.duration, " +
                    "f.mpa_id, m.name AS mpa_name, fg.genre_id, g.name AS genre_name, " +
                    "fd.director_id, d.name AS director_name, COUNT(fl.user_id) AS likes_count " +
                    "FROM films f " +
                    "LEFT JOIN mpa m ON f.mpa_id = m.id " +
                    "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
                    "LEFT JOIN genres g ON fg.genre_id = g.id " +
                    "LEFT JOIN film_directors fd ON f.id = fd.film_id " +
                    "LEFT JOIN directors d ON fd.director_id = d.id " +
                    "LEFT JOIN film_likes fl ON f.id = fl.film_id " +
                    "WHERE fd.director_id = ? " +
                    "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, " +
                    "f.mpa_id, m.name, fg.genre_id, g.name, fd.director_id, d.name " +
                    "ORDER BY likes_count DESC, f.id ASC";

    private static final String GET_FILMS_BY_DIRECTOR_SORTED_BY_YEAR =
            "SELECT f.id AS film_id, f.name, f.description, f.release_date, f.duration, " +
                    "f.mpa_id, m.name AS mpa_name, fg.genre_id, g.name AS genre_name, " +
                    "fd.director_id, d.name AS director_name " +
                    "FROM films f " +
                    "LEFT JOIN mpa m ON f.mpa_id = m.id " +
                    "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
                    "LEFT JOIN genres g ON fg.genre_id = g.id " +
                    "LEFT JOIN film_directors fd ON f.id = fd.film_id " +
                    "LEFT JOIN directors d ON fd.director_id = d.id " +
                    "WHERE fd.director_id = ? " +
                    "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, " +
                    "f.mpa_id, m.name, fg.genre_id, g.name, fd.director_id, d.name " +
                    "ORDER BY f.release_date ASC, f.id ASC";

    private static final String INSERT_DIRECTORS = "INSERT INTO film_directors (film_id, director_id) VALUES (?, ?)";
    private static final String DELETE_DIRECTORS = "DELETE FROM film_directors WHERE film_id = ?";

    public NamedParameterJdbcTemplate npJdbc;

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbc, mapper);
        npJdbc = namedParameterJdbcTemplate;
    }

    public Optional<Film> findById(Long filmId) {
        ResultSetExtractor<Optional<Film>> extractor = rs -> {
            Film film = null;

            while (rs.next()) {
                if (film == null) {
                    film = new Film();
                    setFilmFields(film, filmId, rs);
                }
                //если genreId не null то добавляем его в сет
                setGenre(rs, film);
                setDirector(rs, film);
            }

            return Optional.ofNullable(film);
        };

        return findOne(FIND_BY_ID_QUERY, extractor, filmId);
    }

    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY, getExtractor());
    }

    private ResultSetExtractor<List<Film>> getExtractor() {
        return rs -> {
            Map<Long, Film> filmMap = new HashMap<>();

            while (rs.next()) {
                long filmId = rs.getLong("film_id");
                Film film = filmMap.get(filmId);
                if (film == null) {
                    film = new Film();
                    setFilmFields(film, filmId, rs);
                    filmMap.put(filmId, film);
                }
                //если genreId не null то добавляем его в сет
                setGenre(rs, film);
                setDirector(rs, film);
            }

            return new ArrayList<>(filmMap.values());
        };
    }

    private void setGenre(ResultSet rs, Film film) throws SQLException {
        int genreId = rs.getInt("genre_id");
        if (!rs.wasNull()) {
            Genre genre = new Genre();
            genre.setId(genreId);
            genre.setName(rs.getString("genre_name")); // Достаем имя жанра из базы
            film.getGenres().add(genre);
        }
    }

    private void setDirector(ResultSet rs, Film film) throws SQLException {
        long directorId = rs.getLong("director_id");
        if (!rs.wasNull()) {
            Director director = new Director();
            director.setId(directorId);
            director.setName(rs.getString("director_name"));
            film.getDirectors().add(director);
        }
    }

    private void setFilmFields(Film film, Long filmId, ResultSet rs) throws SQLException {
        film.setId(filmId);
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date") != null ? rs.getDate("release_date").toLocalDate() : null);
        film.setDuration(rs.getInt("duration"));
        film.setGenres(new LinkedHashSet<>());
        film.setDirectors(new LinkedHashSet<>());

        int mpaId = rs.getInt("mpa_id");
        if (!rs.wasNull()) {
            Mpa mpa = new Mpa();
            mpa.setId(mpaId);
            mpa.setName(rs.getString("mpa_name"));
            film.setMpa(mpa);
        }
    }

    public Set<Film> getPopularFilms(Integer limit, Integer genreId, Integer year) {
        List<Integer> params = new ArrayList<>();
        String sqlQuery = getPopularFilmsSqlQuery(genreId, year, params);
        params.add(limit);

        //получение списка id популярных фильмов
        List<Integer> popularFilmIds = jdbc.queryForList(sqlQuery, Integer.class, params.toArray());
        if (popularFilmIds.isEmpty()) return Collections.emptySet();

        //получение всей информации для популярных фильмов
        String inSql = String.join(",", Collections.nCopies(popularFilmIds.size(), "?"));
        String finalQuery = FIND_ALL_QUERY + " WHERE f.id IN (" + inSql + ") " +
                "ORDER BY (SELECT COUNT(*) FROM film_likes WHERE film_id = f.id) DESC, f.id ASC";

        return new LinkedHashSet<>(findMany(finalQuery, getExtractor(), popularFilmIds.toArray()));
    }

    private String getPopularFilmsSqlQuery(Integer genreId, Integer year, List<Integer> params) {
        StringBuilder sqlQuery = new StringBuilder(POPULAR_FILMS_BASE_QUERY);

        boolean hasGenre = genreId != null;
        boolean hasYear = year != null;

        //если жанр передан, то присоединяем таблицу связей жанров с фильмами
        if (hasGenre) {
            sqlQuery.append(JOIN_FILM_GENRES_QUERY);
        }

        //если хотя бы один параметр передан (год/жанр) то добавляем в запрос фильтрацию
        if (hasYear || hasGenre) {
            sqlQuery.append("WHERE ");
            if (hasYear) {
                sqlQuery.append(EXTRACT_YEAR_QUERY);
                params.add(year);
            }
            if (hasGenre) {
                if (hasYear) {
                    sqlQuery.append("AND ");
                }
                sqlQuery.append("fg.genre_id = ? ");
                params.add(genreId);
            }
        }

        sqlQuery.append(GROUP_ORDER_LIMIT_QUERY);

        return sqlQuery.toString();
    }

    @Transactional
    public Film save(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null
        );
        film.setId(id);

        //если список жанров не пустой - сохраняем в бд
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            setGenresToDB(film);
        }

        if (film.getDirectors() != null && !film.getDirectors().isEmpty()) {
            setDirectorsToDB(film);
        }

        return film;
    }

    @Transactional
    public Film update(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null,
                film.getId()
        );

        //если список жанров не пустой - сохраняем в бд
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            //удаление всех жанров фильма
            update(DELETE_GENRES_QUERY, film.getId());
            //сохранение жанров в бд
            setGenresToDB(film);
        }

        if (film.getDirectors() != null) {
            jdbc.update(DELETE_DIRECTORS, film.getId());
            if (!film.getDirectors().isEmpty()) {
                setDirectorsToDB(film);
            }
        }

        return film;
    }

    private void setGenresToDB(Film film) {
        jdbc.batchUpdate(INSERT_GENRES_QUERY, film.getGenres(), film.getGenres().size(),
                (ps, genre) -> {
                    ps.setLong(1, film.getId());
                    ps.setInt(2, genre.getId());
                });
    }

    private void setDirectorsToDB(Film film) {
        jdbc.batchUpdate(INSERT_DIRECTORS, film.getDirectors(), film.getDirectors().size(),
                (ps, director) -> {
                    ps.setLong(1, film.getId());
                    ps.setLong(2, director.getId());
                });
    }

    public void addLike(Long filmId, Long userId) {
        if (!hasLike(filmId, userId)) {
            update(ADD_LIKE_QUERY, filmId, userId);
        }
    }

    public boolean hasLike(Long filmId, Long userId) {
        Integer count = jdbc.queryForObject(FIND_LIKE_QUERY, Integer.class, filmId, userId);
        return count != null && count > 0;
    }

    public void deleteLike(Long filmId, Long userId) {
        delete(DELETE_LIKE_QUERY, filmId, userId);
    }

    public List<Film> getFilmsByRequestParam(String query, List<String> searchType) {
        String userQuery = "%" + query.toLowerCase() + "%";

        StringBuilder sql = new StringBuilder(FIND_ALL_QUERY);

        StringBuilder whereQuery = new StringBuilder("WHERE LOWER(f.name) LIKE ? ");

        List<String> params = new ArrayList<>();
        params.add(userQuery);

        if (searchType.contains("director")) {
            whereQuery.append("OR LOWER(d.name) LIKE ? ");
            params.add(userQuery);
        }

        sql.append(whereQuery);

        ResultSetExtractor<List<Film>> extractor = rs -> {
            Map<Long, Film> filmMap = new LinkedHashMap<>();
            while (rs.next()) {
                long filmId = rs.getLong("film_id");
                Film film = filmMap.get(filmId);
                if (film == null) {
                    film = new Film();
                    setFilmFields(film, filmId, rs);
                    filmMap.put(filmId, film);
                }
                setGenre(rs, film);
                setDirector(rs, film);
            }
            return new ArrayList<>(filmMap.values());
        };
        return findMany(sql.toString(), extractor, params.toArray());
    }

    public void deleteFilm(Long filmId) {
        delete(DELETE_FILM_QUERY, filmId);
    }


    public List<Film> getCommonFilms(Long userId, Long friendId) {
        ResultSetExtractor<List<Film>> extractor = rs -> {
            Map<Long, Film> filmMap = new LinkedHashMap<>();
            while (rs.next()) {
                long filmId = rs.getLong("film_id");
                Film film = filmMap.get(filmId);
                if (film == null) {
                    film = new Film();
                    setFilmFields(film, filmId, rs);
                    filmMap.put(filmId, film);
                }
                //если genreId не null то добавляем его в сет
                setGenre(rs, film);
            }
            return new ArrayList<>(filmMap.values());
        };
        return findMany(SEARCH_COMMON_FILMS, extractor, userId, friendId);
    }

    public List<Film> getRecommendationsFilmsById(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        MapSqlParameterSource parameters = new MapSqlParameterSource("filmIds", ids);

        ResultSetExtractor<List<Film>> extractor = rs -> {
            Map<Long, Film> filmMap = new HashMap<>();
            while (rs.next()) {
                long filmId = rs.getLong("film_id");
                Film film = filmMap.get(filmId);
                if (film == null) {
                    film = new Film();
                    setFilmFields(film, filmId, rs);
                    filmMap.put(filmId, film);
                }
                setGenre(rs, film);
            }

            return new ArrayList<>(filmMap.values());
        };

        return npJdbc.query(GET_RECOMMENDATE_FILMS, parameters, extractor);
    }

    public List<Film> getFilmsByDirectorSortedByLikes(Long directorId) {
        ResultSetExtractor<List<Film>> extractor = rs -> {
            Map<Long, Film> filmMap = new LinkedHashMap<>();
            while (rs.next()) {
                long filmId = rs.getLong("film_id");
                Film film = filmMap.get(filmId);
                if (film == null) {
                    film = new Film();
                    setFilmFields(film, filmId, rs);
                    filmMap.put(filmId, film);
                }
                setGenre(rs, film);
                setDirector(rs, film);
            }
            return new ArrayList<>(filmMap.values());
        };
        return findMany(GET_FILMS_BY_DIRECTOR_SORTED_BY_LIKES, extractor, directorId);
    }

    public List<Film> getFilmsByDirectorSortedByYear(Long directorId) {
        ResultSetExtractor<List<Film>> extractor = rs -> {
            Map<Long, Film> filmMap = new LinkedHashMap<>();
            while (rs.next()) {
                long filmId = rs.getLong("film_id");
                Film film = filmMap.get(filmId);
                if (film == null) {
                    film = new Film();
                    setFilmFields(film, filmId, rs);
                    filmMap.put(filmId, film);
                }
                setGenre(rs, film);
                setDirector(rs, film);
            }
            return new ArrayList<>(filmMap.values());
        };
        return findMany(GET_FILMS_BY_DIRECTOR_SORTED_BY_YEAR, extractor, directorId);
    }

}
