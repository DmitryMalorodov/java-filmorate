package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.user.UserFriendship;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendshipRowMapper implements RowMapper<UserFriendship> {

    @Override
    public UserFriendship mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return UserFriendship.builder()
                .userId(resultSet.getLong("user_id"))
                .friendId(resultSet.getLong("friend_id"))
                .status(resultSet.getString("status"))
                .build();
    }
}
