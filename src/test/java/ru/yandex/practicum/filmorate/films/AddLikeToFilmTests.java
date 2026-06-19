package ru.yandex.practicum.filmorate.films;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.FilmRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.message.FilmValidationMessages.FILM_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.films.FilmData.film;
import static ru.yandex.practicum.filmorate.users.UserData.user;
import static ru.yandex.practicum.filmorate.users.UserData.user2;

@DisplayName("Проверка добавления лайка к фильму")
public class AddLikeToFilmTests extends FilmTest {

    @Autowired
    public AddLikeToFilmTests(FilmRepository filmRepository) {
        super(filmRepository);
    }

    @Test
    void checkAddLikeToFilm() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        //добавление лайков
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId))
                .andExpect(status().isOk());

        //проверка что у фильма с filmId появился лайк от юзера с userId
        Assertions.assertTrue(filmRepository.hasLike(filmId, userId));
    }

    @Test
    void checkAddTwoLikesToFilm() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long userId2 = getIdFromObject(createUser(user2));
        Long filmId = getIdFromObject(createFilm(film));

        //добавление лайков
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId2))
                .andExpect(status().isOk());

        //проверка что у фильма с filmId появился лайк от юзера с userId и от userId2
        Assertions.assertTrue(filmRepository.hasLike(filmId, userId));
        Assertions.assertTrue(filmRepository.hasLike(filmId, userId2));
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

    @Test
    void checkAddLikeToFilmDB() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        //добавление лайков
        filmRepository.addLike(filmId, userId);
        //проверка что у фильма с filmId появился лайк от юзера с userId
        Assertions.assertTrue(filmRepository.hasLike(filmId, userId));
    }
}
