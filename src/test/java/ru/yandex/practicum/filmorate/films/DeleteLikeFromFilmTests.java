package ru.yandex.practicum.filmorate.films;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.endpoint.FilmEndpoints.FILMS_ID;
import static ru.yandex.practicum.filmorate.constant.endpoint.FilmEndpoints.FILMS_ID_LIKE_USER_ID;
import static ru.yandex.practicum.filmorate.constant.message.FilmValidationMessages.FILM_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.films.FilmData.film;
import static ru.yandex.practicum.filmorate.users.UserData.user;

@DisplayName("Проверка удаления лайка с фильма")
public class DeleteLikeFromFilmTests extends FilmTest {

    @Test
    void checkDeleteLikeFromFilm() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        //добавление лайков
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId))
                .andExpect(status().isOk());

        //удаление лайка
        mockMvc.perform(delete(FILMS_ID_LIKE_USER_ID, filmId, userId))
                .andExpect(status().isOk());

        //проверка что у фильма с filmId появился лайк от юзера с userId
        mockMvc.perform(get(FILMS_ID, filmId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likesUsersId").isEmpty());
    }

    @Test
    void checkDeleteLikeFromFilmWithDoesNotExistId() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long notExistFilmId = 10L;

        //добавление лайков
        mockMvc.perform(delete(FILMS_ID_LIKE_USER_ID, notExistFilmId, userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format(FILM_NOT_FOUND_MESSAGE, notExistFilmId)));
    }
}
