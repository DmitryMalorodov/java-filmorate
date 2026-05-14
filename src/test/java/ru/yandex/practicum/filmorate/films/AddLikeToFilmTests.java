package ru.yandex.practicum.filmorate.films;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.endpoint.FilmEndpoints.FILMS_ID;
import static ru.yandex.practicum.filmorate.constant.endpoint.FilmEndpoints.FILMS_ID_LIKE_USER_ID;
import static ru.yandex.practicum.filmorate.constant.message.FilmValidationMessages.FILM_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.films.FilmData.film;
import static ru.yandex.practicum.filmorate.users.UserData.user;

@DisplayName("Проверка добавления лайка к фильму")
public class AddLikeToFilmTests extends FilmTest {

    @Test
    void checkAddLikeToFilm() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        //добавление лайков
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId))
                .andExpect(status().isOk());

        //проверка что у фильма с filmId появился лайк от юзера с userId
        mockMvc.perform(get(FILMS_ID, filmId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likesUsersId[0]").value(1));
    }

    @Test
    void checkAddTwoLikesToFilm() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long userId2 = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        //добавление лайков
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId2))
                .andExpect(status().isOk());

        //проверка что у фильма с filmId появился лайк от юзера с userId и от userId2
        mockMvc.perform(get(FILMS_ID, filmId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likesUsersId[0]").value(1))
                .andExpect(jsonPath("$.likesUsersId[1]").value(2));
    }

    @Test
    void checkEmptyLikeList() throws Exception {
        Long filmId = getIdFromObject(createFilm(film));

        //проверка что у фильма с filmId пустой список лайков
        mockMvc.perform(get(FILMS_ID, filmId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likesUsersId").isEmpty());
    }

    @Test
    void checkAddLikeToFilmWithDoesNotExistId() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long notExistFilmId = 10L;

        //добавление лайков
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, notExistFilmId, userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format(FILM_NOT_FOUND_MESSAGE, notExistFilmId)));
    }
}
