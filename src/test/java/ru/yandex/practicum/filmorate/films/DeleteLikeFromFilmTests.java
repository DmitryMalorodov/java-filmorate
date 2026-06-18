package ru.yandex.practicum.filmorate.films;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.FilmRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.message.FilmValidationMessages.FILM_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.films.FilmData.film;
import static ru.yandex.practicum.filmorate.users.UserData.user;

@DisplayName("Проверка удаления лайка с фильма")
public class DeleteLikeFromFilmTests extends FilmTest {

    @Autowired
    public DeleteLikeFromFilmTests(FilmRepository filmRepository) {
        super(filmRepository);
    }

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

        //проверка что у фильма с filmId нет лайка от юзера с userId
        Assertions.assertFalse(filmRepository.hasLike(filmId, userId));
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

    @Test
    void checkDeleteLikeFromFilmDB() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        //добавление лайков
        filmRepository.addLike(filmId, userId);
        //удаление лайка
        filmRepository.deleteLike(filmId, userId);

        //проверка что у фильма с filmId нет лайка от юзера с userId
        Assertions.assertFalse(filmRepository.hasLike(filmId, userId));
    }
}
