package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(email, name, login, birthday)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, name = ?, birthday = ? WHERE id = ?";
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
                user.getBirthday(),
                user.getId()
        );
        return user;
    }
}
