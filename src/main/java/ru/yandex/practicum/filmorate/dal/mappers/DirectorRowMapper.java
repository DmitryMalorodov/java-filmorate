package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.director.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DirectorRowMapper implements RowMapper<Director> {
    @Override
    public Director mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Director.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
