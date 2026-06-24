package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.review.ReactionType;
import ru.yandex.practicum.filmorate.model.review.ReviewReaction;

import java.util.Optional;

@Repository
public class ReviewReactionRepository extends BaseRepository<ReviewReaction> {

    private static final String FIND_REACTION_REVIEW = "SELECT reaction_type FROM review_reactions " +
            "WHERE review_id = ? AND user_id = ?";

    private static final String INSERT_REACTION_REVIEW = "INSERT INTO review_reactions(user_id, review_id, reaction_type)" +
            "VALUES (?, ?, ?)";

    private static final String DELETE_REACTION_REVIEW = "DELETE FROM review_reactions " +
            "WHERE review_id = ? AND user_id = ?";

    private static final String UPDATE_REACTION_REVIEW = "UPDATE review_reactions SET reaction_type = ?" +
            "WHERE review_id = ? AND user_id = ?";

    public ReviewReactionRepository(JdbcTemplate jdbc, RowMapper<ReviewReaction> mapper) {
        super(jdbc, mapper);
    }

    public Optional<String> getReactionType(Long reviewId, Long userId) {
        return findOneString(FIND_REACTION_REVIEW, reviewId, userId);
    }

    public void addReaction(Long reviewId, Long userId, ReactionType reactionType) {
        jdbc.update(INSERT_REACTION_REVIEW, userId, reviewId, reactionType.name());
    }

    public void removeReaction(Long reviewId, Long userId) {
        jdbc.update(DELETE_REACTION_REVIEW, reviewId, userId);
    }

    public void updateReaction(Long reviewId, Long userId, ReactionType newReactionType) {
        jdbc.update(UPDATE_REACTION_REVIEW, newReactionType.name(), reviewId, userId);
    }
}
