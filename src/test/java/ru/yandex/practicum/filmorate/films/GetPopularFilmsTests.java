package ru.yandex.practicum.filmorate.films;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.constant.message.FilmValidationMessages;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.films.FilmData.film;
import static ru.yandex.practicum.filmorate.films.FilmData.film2;
import static ru.yandex.practicum.filmorate.users.UserData.*;

@DisplayName("Проверка получения списка популярных фильмов по лайкам")
public class GetPopularFilmsTests extends FilmTest {

    @Autowired
    public GetPopularFilmsTests(FilmRepository filmRepository) {
        super(filmRepository);
    }

    @Test
    void checkGetPopularFilmsByLikes() throws Exception {
        prepareTestData();

        //проверка размера списка и что id идут в порядке убывания по кол-ву лайков
        mockMvc.perform(get(FILMS_POPULAR).param("count", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3));
    }

    @Test
    void checkGetPopularFilmsByLikesAndYear() throws Exception {
        prepareTestData();

        //проверка размера списка и что id идут в порядке убывания по кол-ву лайков
        mockMvc.perform(get(FILMS_POPULAR).param("count", "5")
                        .param("year", "2000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void checkGetPopularFilmsByLikesAndGenre() throws Exception {
        prepareTestData();

        //проверка размера списка и что id идут в порядке убывания по кол-ву лайков
        mockMvc.perform(get(FILMS_POPULAR).param("count", "5")
                        .param("genreId", "6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(3));
    }

    @Test
    void checkGetPopularFilmsByLikesYearAndGenre() throws Exception {
        prepareTestData();

        //проверка размера списка и что id идут в порядке убывания по кол-ву лайков
        mockMvc.perform(get(FILMS_POPULAR).param("count", "5")
                        .param("genreId", "1")
                        .param("year", "2000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void checkNegativeValidation() throws Exception {
        mockMvc.perform(get(FILMS_POPULAR).param("count", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(FilmValidationMessages.NEGATIVE_LIMIT_MESSAGE));
    }

    @Test
    void checkGetPopularFilmsDB() throws Exception {
        prepareTestData();

        List<Film> films = filmRepository.getPopularFilms(5, null, null);
        Assertions.assertEquals(3, films.size());
        Assertions.assertEquals(1, films.getFirst().getId());
        Assertions.assertEquals(2, films.get(1).getId());
        Assertions.assertEquals(3, films.getLast().getId());
    }

    private void prepareTestData() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long userId2 = getIdFromObject(createUser(user2));
        Long userId3 = getIdFromObject(createUser(user3));

        Long filmId = getIdFromObject(createFilm(film));
        Long filmId2 = getIdFromObject(createFilm(film));
        Long filmId3 = getIdFromObject(createFilm(film2));

        //добавление лайков к фильму filmId
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId2))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId3))
                .andExpect(status().isOk());

        //добавление лайков к фильму filmId2
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId2, userId))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId2, userId2))
                .andExpect(status().isOk());

        //добавление лайков к фильму filmId3
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId3, userId))
                .andExpect(status().isOk());
    }
}
