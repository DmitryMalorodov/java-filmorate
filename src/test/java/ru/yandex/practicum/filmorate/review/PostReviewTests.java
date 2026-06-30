package ru.yandex.practicum.filmorate.review;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;
import ru.yandex.practicum.filmorate.model.review.Review;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.message.ReviewValidationMessages.CONTENT_BLANK_MESSAGE;
import static ru.yandex.practicum.filmorate.constant.message.ReviewValidationMessages.CONTENT_MAX_LENGTH_MESSAGE;
import static ru.yandex.practicum.filmorate.films.FilmData.film;
import static ru.yandex.practicum.filmorate.review.ReviewData.TOO_LONG_CONTENT;
import static ru.yandex.practicum.filmorate.review.ReviewData.review;
import static ru.yandex.practicum.filmorate.users.UserData.user;

@DisplayName("Проверка добавления отзывов")
public class PostReviewTests extends ReviewTest {

    @Autowired
    public PostReviewTests(ReviewRepository reviewRepository) {
        super(reviewRepository);
    }

    @Test
    void checkCreateReview() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        Review reviewToCreate = review.toBuilder()
                .userId(userId)
                .filmId(filmId)
                .build();

        createReviewRequest(reviewToCreate)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").exists())
                .andExpect(jsonPath("$.content").value(reviewToCreate.getContent()))
                .andExpect(jsonPath("$.isPositive").value(reviewToCreate.getIsPositive()))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.filmId").value(filmId));
    }

    @Test
    void checkContentNullValidation() throws Exception {
        Review invalid = review.toBuilder().content(null).build();
        checkValidationError(createReviewRequest(invalid), CONTENT_BLANK_MESSAGE);
    }

    @Test
    void checkContentBlankValidation() throws Exception {
        Review invalid = review.toBuilder().content(" ").build();
        checkValidationError(createReviewRequest(invalid), CONTENT_BLANK_MESSAGE);
    }

    @Test
    void checkTooLongContentValidation() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        Review invalid = review.toBuilder()
                .userId(userId)
                .filmId(filmId)
                .content(TOO_LONG_CONTENT)
                .build();

        checkValidationError(createReviewRequest(invalid), CONTENT_MAX_LENGTH_MESSAGE);
    }

    @Test
    void checkCreateReviewDB() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        Review reviewToSave = review.toBuilder()
                .userId(userId)
                .filmId(filmId)
                .build();

        Review savedReview = reviewRepository.save(reviewToSave);

        Assertions.assertNotNull(savedReview.getReviewId());
        Assertions.assertEquals(reviewToSave.getContent(), savedReview.getContent());
        Assertions.assertEquals(reviewToSave.getIsPositive(), savedReview.getIsPositive());
    }
}