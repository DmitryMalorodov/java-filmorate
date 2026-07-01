package ru.yandex.practicum.filmorate.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.model.event.OperationType;
import ru.yandex.practicum.filmorate.model.review.Review;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.films.FilmData.film;
import static ru.yandex.practicum.filmorate.review.ReviewData.review;
import static ru.yandex.practicum.filmorate.review.ReviewTest.REVIEWS_ID;
import static ru.yandex.practicum.filmorate.users.UserData.user;

@DisplayName("Проверка получения событий по ревью фильмов")
public class ReviewEventTests extends EventTest {

    @Test
    void addReviewEvent() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        Long reviewId = getReviewIdFromObject(createReviewRequest(review.toBuilder()
                .userId(userId)
                .filmId(filmId)
                .build()));

        //проверка получения event по операции добавления лайка
        mockMvc.perform(get(USERS_ID_FEED, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].eventId").exists())
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[0].entityId").value(reviewId))
                .andExpect(jsonPath("$[0].eventType").value(EventType.REVIEW.toString()))
                .andExpect(jsonPath("$[0].operation").value(OperationType.ADD.toString()))
                .andExpect(jsonPath("$[0].timestamp").exists());
    }

    @Test
    void updateReviewEvent() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        Review created = createReview(review.toBuilder()
                .userId(userId)
                .filmId(filmId)
                .build());

        Review updatedReview = review.toBuilder()
                .reviewId(created.getReviewId())
                .content("Обновленный отзыв — стал ещё лучше!")
                .isPositive(false)
                .userId(userId)
                .filmId(filmId)
                .build();

        changeReview(updatedReview);

        //проверка получения event по операции добавления лайка
        mockMvc.perform(get(USERS_ID_FEED, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].eventId").exists())
                .andExpect(jsonPath("$[1].userId").value(userId))
                .andExpect(jsonPath("$[1].entityId").value(created.getReviewId()))
                .andExpect(jsonPath("$[1].eventType").value(EventType.REVIEW.toString()))
                .andExpect(jsonPath("$[1].operation").value(OperationType.UPDATE.toString()))
                .andExpect(jsonPath("$[1].timestamp").exists());
    }

    @Test
    void deleteReviewEvent() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        Long reviewId = getReviewIdFromObject(createReviewRequest(review.toBuilder()
                .userId(userId)
                .filmId(filmId)
                .build()));

        mockMvc.perform(delete(REVIEWS_ID, reviewId))
                .andExpect(status().isOk());

        //проверка получения event по операции добавления лайка
        mockMvc.perform(get(USERS_ID_FEED, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].eventId").exists())
                .andExpect(jsonPath("$[1].userId").value(userId))
                .andExpect(jsonPath("$[1].entityId").value(reviewId))
                .andExpect(jsonPath("$[1].eventType").value(EventType.REVIEW.toString()))
                .andExpect(jsonPath("$[1].operation").value(OperationType.REMOVE.toString()))
                .andExpect(jsonPath("$[1].timestamp").exists());
    }
}
