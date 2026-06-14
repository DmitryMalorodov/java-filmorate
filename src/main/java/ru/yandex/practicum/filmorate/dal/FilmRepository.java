package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
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

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Film> findById(Long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public List<Film> getPopularFilms(int limit) {
        return findMany(POPULAR_FILMS_QUERY, limit);
    }

    public Film save(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaId()
        );
        film.setId(id);
        return film;
    }

    public Film update(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaId(),
                film.getId()
        );
        return film;
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
}
