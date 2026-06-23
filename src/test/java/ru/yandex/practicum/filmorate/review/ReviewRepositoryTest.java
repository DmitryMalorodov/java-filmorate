package ru.yandex.practicum.filmorate.review;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;
import ru.yandex.practicum.filmorate.dal.mappers.ReviewRowMapper;
import ru.yandex.practicum.filmorate.model.review.Review;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({ReviewRepository.class, ReviewRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ReviewRepositoryTest {

    private final ReviewRepository reviewRepository;
    private final JdbcTemplate jdbcTemplate;
    private Long testReviewId;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM reviews");
        jdbcTemplate.execute("DELETE FROM films");
        jdbcTemplate.execute("DELETE FROM users");

        jdbcTemplate.update(
                "INSERT INTO users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)",
                1L, "test@test.com", "testuser", "Test User", LocalDate.of(2000, 1, 1)
        );

        jdbcTemplate.update(
                "INSERT INTO films (id, name, description, release_date, duration) VALUES (?, ?, ?, ?, ?)",
                1L, "Test Film", "Test Description", LocalDate.of(2020, 1, 1), 120
        );

        jdbcTemplate.update(
                "INSERT INTO users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)",
                2L, "test2@test.com", "testuser2", "Test User 2", LocalDate.of(2000, 1, 1)
        );

        jdbcTemplate.update(
                "INSERT INTO films (id, name, description, release_date, duration) VALUES (?, ?, ?, ?, ?)",
                2L, "Test Film 2", "Test Description 2", LocalDate.of(2020, 1, 1), 120
        );

        Review review = Review.builder()
                .content("Test Review")
                .isPositive(true)
                .userId(1L)
                .filmId(1L)
                .build();

        Review saved = reviewRepository.save(review);
        testReviewId = saved.getReviewId();
    }
    @Test
    void testSave() {
        Review review = Review.builder()
                .content("New Review")
                .isPositive(false)
                .userId(2L)
                .filmId(2L)
                .build();

        Review saved = reviewRepository.save(review);

        assertThat(saved.getReviewId()).isNotNull();
        assertThat(saved.getContent()).isEqualTo("New Review");
        assertThat(saved.getIsPositive()).isFalse();
        assertThat(saved.getUserId()).isEqualTo(2L);
        assertThat(saved.getFilmId()).isEqualTo(2L);
        assertThat(saved.getUseful()).isEqualTo(0);
        assertThat(saved.getLikesCount()).isEqualTo(0);
        assertThat(saved.getDislikesCount()).isEqualTo(0);
    }

    @Test
    void testFindById() {
        Optional<Review> found = reviewRepository.findById(testReviewId);

        assertThat(found).isPresent();
        assertThat(found.get().getContent()).isEqualTo("Test Review");
        assertThat(found.get().getIsPositive()).isTrue();
        assertThat(found.get().getUserId()).isEqualTo(1L);
        assertThat(found.get().getFilmId()).isEqualTo(1L);
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Review> found = reviewRepository.findById(999L);
        assertThat(found).isEmpty();
    }

    @Test
    void testFindAll() {
        List<Review> reviews = reviewRepository.findAll();
        assertThat(reviews).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void testFindByFilmId() {
        List<Review> reviews = reviewRepository.findByFilmId(1L, 10);
        assertThat(reviews).hasSizeGreaterThanOrEqualTo(1);
        assertThat(reviews.get(0).getFilmId()).isEqualTo(1L);
    }

    @Test
    void testFindByFilmIdNull() {
        List<Review> reviews = reviewRepository.findByFilmId(null, 10);
        assertThat(reviews).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void testUpdate() {
        Optional<Review> found = reviewRepository.findById(testReviewId);
        assertThat(found).isPresent();

        Review review = found.get();
        review.setContent("Updated Content");
        review.setIsPositive(false);

        reviewRepository.update(review);

        Optional<Review> updated = reviewRepository.findById(testReviewId);
        assertThat(updated).isPresent();
        assertThat(updated.get().getContent()).isEqualTo("Updated Content");
        assertThat(updated.get().getIsPositive()).isFalse();
    }

    @Test
    void testDelete() {
        reviewRepository.delete(testReviewId);
        Optional<Review> found = reviewRepository.findById(testReviewId);
        assertThat(found).isEmpty();
    }

    @Test
    void testGetLikesCount() {
        int likes = reviewRepository.getLikesCount(testReviewId);
        assertThat(likes).isEqualTo(0);
    }

    @Test
    void testGetDislikesCount() {
        int dislikes = reviewRepository.getDislikesCount(testReviewId);
        assertThat(dislikes).isEqualTo(0);
    }

    @Test
    void testUpdateCounters() {
        reviewRepository.updateCounters(testReviewId, 1, 0);
        int likes = reviewRepository.getLikesCount(testReviewId);
        assertThat(likes).isEqualTo(1);

        reviewRepository.updateCounters(testReviewId, 0, 1);
        int dislikes = reviewRepository.getDislikesCount(testReviewId);
        assertThat(dislikes).isEqualTo(1);

        int useful = likes - dislikes;
        assertThat(useful).isEqualTo(0);
    }
}