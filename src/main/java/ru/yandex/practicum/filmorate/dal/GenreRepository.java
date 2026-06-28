package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<Genre> {
    private static final String FIND_ALL_GENRES_QUERY = "SELECT * FROM genres ORDER BY id";
    private static final String FIND_GENRE_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String FIND_GENRES_BY_FILM_ID = "SELECT g.* FROM genres g" +
            "JOIN film_genres fg ON g.id = fg.genre_id" +
            "WHERE fg.film_id = ?" +
            "ORDER BY g.id";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> findAll() {
        return findMany(FIND_ALL_GENRES_QUERY);
    }

    public Optional<Genre> findById(Long filmId) {
        return findOne(FIND_GENRE_BY_ID_QUERY, filmId);
    }

    public List<Genre> findGenresByFilmId(Long filmId) {
        return findMany(FIND_GENRES_BY_FILM_ID, filmId);
    }
}
