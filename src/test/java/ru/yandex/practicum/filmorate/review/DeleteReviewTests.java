package ru.yandex.practicum.filmorate.review;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.message.ReviewValidationMessages.REVIEW_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.films.FilmData.film;
import static ru.yandex.practicum.filmorate.review.ReviewData.review;
import static ru.yandex.practicum.filmorate.users.UserData.user;

@DisplayName("Проверка удаления отзыва")
public class DeleteReviewTests extends ReviewTest {

    @Autowired
    public DeleteReviewTests(ReviewRepository reviewRepository) {
        super(reviewRepository);
    }

    @Test
    void checkDeleteReview() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        Long reviewId = getReviewIdFromObject(createReviewRequest(review.toBuilder()
                .userId(userId)
                .filmId(filmId)
                .build()));

        mockMvc.perform(delete(REVIEWS_ID, reviewId))
                .andExpect(status().isOk());

        mockMvc.perform(get(REVIEWS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void checkDeleteReviewWithDoesNotExistId() throws Exception {
        Long notExistId = 999L;
        mockMvc.perform(delete(REVIEWS_ID, notExistId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format(REVIEW_NOT_FOUND_MESSAGE, notExistId)));
    }

    @Test
    void checkDeleteReviewDB() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        Long reviewId = getReviewIdFromObject(createReviewRequest(review.toBuilder()
                .userId(userId)
                .filmId(filmId)
                .build()));

        reviewRepository.delete(reviewId);
        Assertions.assertTrue(reviewRepository.findById(reviewId).isEmpty());
    }
}