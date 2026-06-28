package ru.yandex.practicum.filmorate.review;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;
import ru.yandex.practicum.filmorate.model.review.Review;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.message.ReviewValidationMessages.*;
import static ru.yandex.practicum.filmorate.films.FilmData.film;
import static ru.yandex.practicum.filmorate.review.ReviewData.*;
import static ru.yandex.practicum.filmorate.users.UserData.user;

@DisplayName("Проверка изменения отзывов")
public class PutReviewTests extends ReviewTest {

    @Autowired
    public PutReviewTests(ReviewRepository reviewRepository) {
        super(reviewRepository);
    }

    @Test
    void checkUpdateReview() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        Review created = createReview(review.toBuilder()
                .userId(userId)
                .filmId(filmId)
                .build());

        Review updatedReview = review.toBuilder()
                .id(created.getId())
                .content("Обновленный отзыв — стал ещё лучше!")
                .isPositive(false)
                .userId(userId)
                .filmId(filmId)
                .build();

        changeReview(updatedReview)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(created.getId()))
                .andExpect(jsonPath("$.content").value(updatedReview.getContent()))
                .andExpect(jsonPath("$.isPositive").value(false));
    }

    @Test
    void checkContentNullValidation() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        Review created = createReview(review.toBuilder()
                .userId(userId)
                .filmId(filmId)
                .build());

        Review invalid = created.toBuilder().content(null).build();

        checkValidationError(changeReview(invalid), CONTENT_BLANK_MESSAGE);
    }

    @Test
    void checkContentBlankValidation() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        Review created = createReview(review.toBuilder()
                .userId(userId)
                .filmId(filmId)
                .build());

        Review invalid = created.toBuilder().content(" ").build();

        checkValidationError(changeReview(invalid), CONTENT_BLANK_MESSAGE);
    }

    @Test
    void checkTooLongContentValidation() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        Review created = createReview(review.toBuilder()
                .userId(userId)
                .filmId(filmId)
                .build());

        Review invalid = created.toBuilder()
                .content(TOO_LONG_CONTENT)
                .build();

        checkValidationError(changeReview(invalid), CONTENT_MAX_LENGTH_MESSAGE);
    }

    @Test
    void checkUpdateReviewDB() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        Review created = createReview(review.toBuilder()
                .userId(userId)
                .filmId(filmId)
                .build());

        Review updated = review.toBuilder()
                .id(created.getId())
                .content("Обновленный контент в БД")
                .isPositive(false)
                .userId(userId)
                .filmId(filmId)
                .build();

        Review actReview = reviewRepository.update(updated);

        SoftAssertions softAssert = new SoftAssertions();
        checkReview(actReview, updated, softAssert);
        softAssert.assertAll();
    }
}