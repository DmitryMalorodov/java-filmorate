package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.user.UserFriendship;

import java.util.List;

@Repository
public class FriendshipRepository extends BaseRepository<UserFriendship> {
    private static final String FIND_FRIENDSHIP_QUERY = "SELECT status FROM user_friendships WHERE user_id = ?" +
            " AND friend_id = ?";
    private static final String SET_CONFIRMED_QUERY = "UPDATE user_friendships SET status = 'CONFIRMED'" +
            " WHERE user_id = ? AND friend_id = ?";
    private static final String INSERT_FRIENDSHIP_QUERY = "INSERT INTO user_friendships (user_id, friend_id, status)" +
            " VALUES (?, ?, 'PENDING')";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM user_friendships WHERE" +
            " (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";

    public FriendshipRepository(JdbcTemplate jdbc, RowMapper<UserFriendship> mapper) {
        super(jdbc, mapper);
    }

    public void addFriend(Long userId, Long friendId) {
        //проверка была ли ранее уже обратная заявка от friendId к userId
        if (!getFriendshipStatus(friendId, userId).isEmpty()) {
            //если была, то устанавливаем ей статус CONFIRMED
            update(SET_CONFIRMED_QUERY, friendId, userId);
        } else {
            //проверка, что не было заявки ранее от userId к friendId
            if (getFriendshipStatus(userId, friendId).isEmpty()) {
                //создание новой заявки со статусом PENDING
                update(INSERT_FRIENDSHIP_QUERY, userId, friendId);
            }
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        // удаление записи дружбы из БД
        delete(DELETE_FRIEND_QUERY, userId, friendId, friendId, userId);
    }

    private List<String> getFriendshipStatus(Long userId, Long friendId) {
        return findMany(FIND_FRIENDSHIP_QUERY, "status", userId, friendId);
    }
}