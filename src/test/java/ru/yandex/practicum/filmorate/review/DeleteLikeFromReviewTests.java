package ru.yandex.practicum.filmorate.review;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.ReviewReactionRepository;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.message.ReviewValidationMessages.REVIEW_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.films.FilmData.film;
import static ru.yandex.practicum.filmorate.review.ReviewData.review;
import static ru.yandex.practicum.filmorate.users.UserData.user;

@DisplayName("Проверка удаления лайка с отзыва")
public class DeleteLikeFromReviewTests extends ReviewTest {

    @Autowired
    private ReviewReactionRepository reviewReactionRepository;

    @Autowired
    public DeleteLikeFromReviewTests(ReviewRepository reviewRepository) {
        super(reviewRepository);
    }

    @Test
    void checkDeleteLikeFromReview() throws Exception {
        // Создаём пользователя и фильм
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        Long reviewId = getReviewIdFromObject(createReviewRequest(review.toBuilder()
                .userId(userId)
                .filmId(filmId)
                .build()));

        mockMvc.perform(put(REVIEWS_ID_LIKE, reviewId, userId))
                .andExpect(status().isOk());

        mockMvc.perform(delete(REVIEWS_ID_LIKE, reviewId, userId))
                .andExpect(status().isOk());

        Assertions.assertTrue(reviewReactionRepository.getReactionType(reviewId, userId).isEmpty(),
                "Лайк должен быть удалён");
    }

    @Test
    void checkDeleteLikeFromReviewWithDoesNotExistReview() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long notExistReviewId = 999L;

        mockMvc.perform(delete(REVIEWS_ID_LIKE, notExistReviewId, userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format(REVIEW_NOT_FOUND_MESSAGE, notExistReviewId)));
    }
}