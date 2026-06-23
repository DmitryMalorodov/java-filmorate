package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class FilmRepository extends BaseRepository<Film> {
    private static final String FIND_ALL_QUERY = "SELECT f.id AS film_id, f.name, f.description, f.release_date, f.duration, " +
            "f.mpa_id, m.name AS mpa_name, fg.genre_id, g.name AS genre_name " +
            "FROM films f " +
            "LEFT JOIN mpa m ON f.mpa_id = m.id " +
            "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
            "LEFT JOIN genres g ON fg.genre_id = g.id";
    private static final String FIND_BY_ID_QUERY = "SELECT f.id AS film_id, f.name, f.description, f.release_date, f.duration, " +
            "f.mpa_id, m.name AS mpa_name, fg.genre_id, g.name AS genre_name " +
            "FROM films f " +
            "LEFT JOIN mpa m ON f.mpa_id = m.id " +
            "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
            "LEFT JOIN genres g ON fg.genre_id = g.id " +
            "WHERE f.id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, mpa_id)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?," +
            " duration = ?, mpa_id = ? WHERE id = ?";
    private static final String FIND_LIKE_QUERY = "SELECT COUNT(*) FROM film_likes WHERE film_id = ? AND user_id = ?";
    private static final String ADD_LIKE_QUERY = "INSERT INTO film_likes(film_id, user_id)" +
            "VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
    private static final String POPULAR_FILMS_QUERY = "SELECT f.* FROM films f " +
            "LEFT JOIN film_likes fl ON f.id = fl.film_id " +
            "GROUP BY f.id " +
            "ORDER BY COUNT(fl.user_id) DESC " +
            "LIMIT ?";
    private static final String INSERT_GENRES_QUERY = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_GENRES_QUERY = "DELETE FROM film_genres WHERE film_id = ?";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
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
            }

            return Optional.ofNullable(film);
        };

        return findOne(FIND_BY_ID_QUERY, extractor, filmId);
    }

    public List<Film> findAll() {
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
                //если genreId не null то добавляем его в сет
                setGenre(rs, film);
            }

            return new ArrayList<>(filmMap.values());
        };

        return findMany(FIND_ALL_QUERY, extractor);
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

    private void setFilmFields(Film film, Long filmId, ResultSet rs) throws SQLException {
        film.setId(filmId);
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date") != null ? rs.getDate("release_date").toLocalDate() : null);
        film.setDuration(rs.getInt("duration"));
        film.setGenres(new LinkedHashSet<>());

        int mpaId = rs.getInt("mpa_id");
        if (!rs.wasNull()) {
            Mpa mpa = new Mpa();
            mpa.setId(mpaId);
            mpa.setName(rs.getString("mpa_name"));
            film.setMpa(mpa);
        }
    }

    public List<Film> getPopularFilms(int limit) {
        return findMany(POPULAR_FILMS_QUERY, limit);
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

        return film;
    }

    private void setGenresToDB(Film film) {
        jdbc.batchUpdate(INSERT_GENRES_QUERY, film.getGenres(), film.getGenres().size(),
                (ps, genre) -> {
                    ps.setLong(1, film.getId());
                    ps.setInt(2, genre.getId());
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

    public List<Film> getFilmsByRequestParam(String queryDB, Object[] params) {
        return findMany(queryDB, params);
    }
}
