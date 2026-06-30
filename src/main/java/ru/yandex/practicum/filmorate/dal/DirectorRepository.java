package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.DirectorRowMapper;
import ru.yandex.practicum.filmorate.model.director.Director;

import java.util.List;
import java.util.Optional;


@Repository
public class DirectorRepository extends BaseRepository<Director> {

    private static final String FIND_ALL = "SELECT * FROM directors ORDER BY id";
    private static final String FIND_BY_ID = "SELECT * FROM directors WHERE id = ?";
    private static final String INSERT = "INSERT INTO directors (name) VALUES (?)";
    private static final String UPDATE = "UPDATE directors SET name = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM directors WHERE id = ?";
    private static final String FIND_DIRECTORS_BY_FILM_ID =
            "SELECT d.* FROM directors d " +
                    "JOIN film_directors fd ON d.id = fd.director_id " +
                    "WHERE fd.film_id = ? " +
                    "ORDER BY d.id";

    public DirectorRepository(JdbcTemplate jdbc, DirectorRowMapper mapper) {
        super(jdbc, mapper);
    }

    public List<Director> findAll() {
        return findMany(FIND_ALL);
    }

    public Optional<Director> findById(Long id) {
        return findOne(FIND_BY_ID, id);
    }

    public Director save(Director director) {
        Long id = insert(INSERT, director.getName());
        director.setId(id);
        return director;
    }

    public Director update(Director director) {
        update(UPDATE, director.getName(), director.getId());
        return director;
    }

    public boolean deleteById(Long id) {
        return delete(DELETE, id);
    }

    public List<Director> findDirectorsByFilmId(Long filmId) {
        return findMany(FIND_DIRECTORS_BY_FILM_ID, filmId);
    }
}