package ru.yandex.practicum.filmorate.review;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;
import ru.yandex.practicum.filmorate.model.review.Review;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.message.ReviewValidationMessages.REVIEW_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.films.FilmData.film;
import static ru.yandex.practicum.filmorate.review.ReviewData.review;
import static ru.yandex.practicum.filmorate.users.UserData.user;

@DisplayName("Проверка получения отзывов")
public class GetReviewsTests extends ReviewTest {

    @Autowired
    public GetReviewsTests(ReviewRepository reviewRepository) {
        super(reviewRepository);
    }

    @Test
    void checkGettingOneReview() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        Review created = createReview(review.toBuilder()
                .userId(userId)
                .filmId(filmId)
                .build());

        mockMvc.perform(get(REVIEWS_ID, created.getReviewId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(created.getReviewId()))
                .andExpect(jsonPath("$.content").value(review.getContent()))
                .andExpect(jsonPath("$.isPositive").value(review.getIsPositive()))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.filmId").value(filmId));
    }

    @Test
    void checkGetDoesNotExistReview() throws Exception {
        Long reviewNotExistId = 999L;

        mockMvc.perform(get(REVIEWS_ID, reviewNotExistId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format(REVIEW_NOT_FOUND_MESSAGE, reviewNotExistId)));
    }

    @Test
    void checkGettingOneReviewDB() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        Review created = createReview(review.toBuilder()
                .userId(userId)
                .filmId(filmId)
                .build());

        Optional<Review> optReview = reviewRepository.findById(created.getReviewId());
        Assertions.assertTrue(optReview.isPresent());

        SoftAssertions softAssert = new SoftAssertions();
        checkReview(optReview.get(), created, softAssert);
        softAssert.assertAll();
    }

    @Test
    void checkGetDoesNotExistReviewDB() {
        Long reviewNotExistId = 999L;
        Assertions.assertTrue(reviewRepository.findById(reviewNotExistId).isEmpty());
    }
}