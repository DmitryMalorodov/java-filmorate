package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.*;

@Repository
public class UserRepository extends BaseRepository<User> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(email, name, login, birthday)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, name = ?, login = ?, birthday = ? WHERE id = ?";
    private static final String COMMON_FRIENDS_QUERY = "SELECT u.* FROM users u " +
            "WHERE u.id IN (" +
            "SELECT friend_id FROM user_friendships WHERE user_id = ? " +
            "UNION " +
            "SELECT user_id FROM user_friendships WHERE friend_id = ? AND status = 'CONFIRMED' " +
            ") " +
            "AND u.id IN (" +
            "SELECT friend_id FROM user_friendships WHERE user_id = ? " +
            "UNION " +
            "SELECT user_id FROM user_friendships WHERE friend_id = ? AND status = 'CONFIRMED' " +
            ")";
    private static final String USER_FRIENDS_QUERY = "SELECT u.* FROM users u " +
            "WHERE u.id IN (" +
            "SELECT friend_id FROM user_friendships WHERE user_id = ? " +
            "UNION " +
            "SELECT user_id FROM user_friendships WHERE friend_id = ? AND status = 'CONFIRMED' " +
            ")";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String GET_RECOMMENDED_FILM_IDS = "SELECT fl.film_id " +
                    "FROM film_likes fl " +
                    "WHERE fl.user_id IN ( " +
                    "    SELECT sim.user_id " +
                    "    FROM film_likes sim " +
                    "    INNER JOIN film_likes target ON sim.film_id = target.film_id " +
                    "    WHERE target.user_id = ? AND sim.user_id <> ? " +
                    "    GROUP BY sim.user_id " +
                    "    ORDER BY COUNT(sim.film_id) DESC " +
                    "    LIMIT 1 " +
                    ") " +
                    "AND fl.film_id NOT IN (SELECT film_id FROM film_likes WHERE user_id = ?) " +
                    "GROUP BY fl.film_id " +
                    "ORDER BY COUNT(fl.user_id) DESC";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public Optional<User> findById(Long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public List<User> getCommonFriendsList(Long userId, Long otherUserId) {
        return findMany(COMMON_FRIENDS_QUERY, userId, userId, otherUserId, otherUserId);
    }

    public List<User> getFriendsList(Long userId) {
        return findMany(USER_FRIENDS_QUERY, userId, userId);
    }

    public User save(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    public void deleteUser(Long userId) {
        delete(DELETE_USER_QUERY, userId);
    }

    public List<Long> getRecommendedFilmIds(Long userId) {
        return jdbc.queryForList(GET_RECOMMENDED_FILM_IDS, Long.class, userId, userId, userId);
    }

}
