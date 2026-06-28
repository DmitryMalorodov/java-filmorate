package ru.yandex.practicum.filmorate.review;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.ReviewReactionRepository;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;
import ru.yandex.practicum.filmorate.model.review.ReactionType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.message.ReviewValidationMessages.REVIEW_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.films.FilmData.film;
import static ru.yandex.practicum.filmorate.review.ReviewData.*;
import static ru.yandex.practicum.filmorate.users.UserData.user;

@DisplayName("Проверка добавления дизлайка к отзыву")
public class AddDislikeToReviewTests extends ReviewTest {

    @Autowired
    private ReviewReactionRepository reviewReactionRepository;

    @Autowired
    public AddDislikeToReviewTests(ReviewRepository reviewRepository) {
        super(reviewRepository);
    }

    @Test
    void checkAddDislikeToReview() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        Long id = getReviewIdFromObject(createReviewRequest(review.toBuilder()
                .userId(userId)
                .filmId(filmId)
                .build()));

        mockMvc.perform(put(REVIEWS_ID_DISLIKE, id, userId))
                .andExpect(status().isOk());

        Assertions.assertTrue(reviewReactionRepository.getReactionType(id, userId)
                .filter(rt -> rt.equals(ReactionType.DISLIKE.name()))
                .isPresent());
    }

    @Test
    void checkAddDislikeToReviewWithDoesNotExistReview() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long notExistReviewId = 999L;

        mockMvc.perform(put(REVIEWS_ID_DISLIKE, notExistReviewId, userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format(REVIEW_NOT_FOUND_MESSAGE, notExistReviewId)));
    }
}