package ru.yandex.practicum.filmorate.recommendations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.MainTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.films.FilmData.film;
import static ru.yandex.practicum.filmorate.users.UserData.*;

@DisplayName("Проверка рекомендованных фильмов")
public class RecommendationTests extends MainTest {

    static final String FILMS_ID_LIKE_USER_ID = "/films/{id}/like/{userId}";
    static final String REST_REQUEST = "/users/{id}/recommendations";

    @Test
    public void testRecommendFilms() throws Exception {
        Long targetUserId = prepareTestData();

        mockMvc.perform(get(REST_REQUEST, targetUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    public void testWhenUserLikedALlFilms() throws Exception {
        Long targetUserId = prepareTestData();

        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, 2, targetUserId));

        mockMvc.perform(get(REST_REQUEST, targetUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testWhenNewUserNotHaveLikedList() throws Exception {
        Long targetUserId = prepareTestData();
        //удаляю лайк с фильма теперь пользователь как будто только зарегестрировался
        mockMvc.perform(delete("/films/1/like/{idUser}", targetUserId))
                .andExpect(status().isOk());

        mockMvc.perform(get(REST_REQUEST, targetUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    private Long prepareTestData() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long userId2 = getIdFromObject(createUser(user2));
        Long userId3 = getIdFromObject(createUser(user3));

        Long filmId = getIdFromObject(createFilm(film));
        Long filmId2 = getIdFromObject(createFilm(film));

        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId2))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId3))
                .andExpect(status().isOk());

        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId2, userId))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId2, userId2))
                .andExpect(status().isOk());

        return userId3;
    }

}
