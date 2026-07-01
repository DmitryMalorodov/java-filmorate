package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.ReviewRowMapper;
import ru.yandex.practicum.filmorate.model.review.Review;

import java.util.List;
import java.util.Optional;

@Repository
public class ReviewRepository extends BaseRepository<Review> {

    private static final String FIND_ALL_REVIEW = "SELECT * FROM reviews";

    private static final String FIND_BY_ID_REVIEW = "SELECT * FROM reviews WHERE review_id = ?";

    private static final String FIND_BY_FILM = "SELECT * FROM reviews WHERE film_id = ? ORDER BY useful DESC LIMIT ?";

    private static final String INSERT_REVIEW = "INSERT INTO reviews(content, is_positive, user_id, film_id) " +
            "VALUES (?, ?, ?, ?)";

    private static final String UPDATE_REVIEW = "UPDATE reviews SET content = ?, is_positive = ?, user_id = ?, " +
            "film_id = ?, useful = ? WHERE review_id = ?";

    private static final String DELETE_REVIEW = "DELETE FROM reviews WHERE review_id = ?";

    private static final String GET_LIKES = "SELECT likes_count FROM reviews WHERE review_id = ?";

    private static final String GET_DISLIKES = "SELECT dislikes_count FROM reviews WHERE review_id = ?";

    private static final String UPDATE_USEFUL = "UPDATE reviews SET useful = ? WHERE review_id = ?";

    private static final String UPDATE_LIKES = "UPDATE reviews SET likes_count = ? WHERE review_id = ?";

    private static final String UPDATE_DISLIKES = "UPDATE reviews SET dislikes_count = ? WHERE review_id = ?";

    public ReviewRepository(JdbcTemplate jdbc, ReviewRowMapper reviewRowMapper) {
        super(jdbc, reviewRowMapper);
    }

    public Optional<Review> findById(Long reviewId) {
        return findOne(FIND_BY_ID_REVIEW, reviewId);
    }

    public List<Review> findAll() {
        return findMany(FIND_ALL_REVIEW);
    }

    public List<Review> findByFilmId(Long filmId, int count) {
        if (filmId == null) {
            return findMany(FIND_ALL_REVIEW + " ORDER BY useful DESC LIMIT ? ", count);
        }
        return findMany(FIND_BY_FILM, filmId, count);
    }

    public Review save(Review review) {
        Long reviewId = insert(
                INSERT_REVIEW,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId()
        );
        review.setReviewId(reviewId);
        review.setUseful(0);
        review.setLikesCount(0);
        review.setDislikesCount(0);
        return review;
    }

    public Review update(Review review) {
        update(
                UPDATE_REVIEW,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getUseful(),
                review.getReviewId()
        );
        return review;
    }

    public void delete(Long reviewId) {
        delete(DELETE_REVIEW, reviewId);
    }

    public int getLikesCount(Long reviewId) {
        return count(GET_LIKES, reviewId);
    }

    public int getDislikesCount(Long reviewId) {
        return count(GET_DISLIKES, reviewId);
    }

    public void updateCounters(Long reviewId, int likes, int dislikes) {
        if (likes != 0) {
            jdbc.update(UPDATE_LIKES, getLikesCount(reviewId) + likes, reviewId);
        }
        if (dislikes != 0) {
            jdbc.update(UPDATE_DISLIKES, getDislikesCount(reviewId) + dislikes, reviewId);
        }
        updateUseful(reviewId);
    }

    public void updateUseful(Long reviewId) {
        int likes = getLikesCount(reviewId);
        int dislikes = getDislikesCount(reviewId);
        int useful = likes - dislikes;
        jdbc.update(UPDATE_USEFUL, useful, reviewId);
    }
}
