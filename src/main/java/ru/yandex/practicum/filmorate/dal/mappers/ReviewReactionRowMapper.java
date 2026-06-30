package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.review.ReviewReaction;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewReactionRowMapper implements RowMapper<ReviewReaction> {

    @Override
    public ReviewReaction mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        ReviewReaction reaction = new ReviewReaction();
        reaction.setId(resultSet.getLong("id"));
        reaction.setUserId(resultSet.getLong("user_id"));
        reaction.setReviewId(resultSet.getLong("review_id"));
        reaction.setReactionType(resultSet.getString("reaction_type"));
        return reaction;
    }
}