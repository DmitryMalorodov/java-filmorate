package ru.yandex.practicum.filmorate.films;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.endpoint.FilmEndpoints.FILMS_ID;
import static ru.yandex.practicum.filmorate.constant.message.FilmValidationMessages.FILM_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.films.FilmData.film;

@DisplayName("Проверка получения фильма")
public class GetFilmTests extends FilmTest {

    @Autowired
    public GetFilmTests(FilmRepository filmRepository) {
        super(filmRepository);
    }

    @Test
    void checkGettingOneFilm() throws Exception {
        Long filmId = getIdFromObject(createFilm(film));

        mockMvc.perform(get(FILMS_ID, filmId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(film.getName()))
                .andExpect(jsonPath("$.description").value(film.getDescription()))
                .andExpect(jsonPath("$.releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("$.duration").value(film.getDuration()))
                .andExpect(jsonPath("$.mpa.id").value(film.getMpa().getId()))
                .andExpect(jsonPath("$.mpa.name").value(film.getMpa().getName()));
    }

    @Test
    void checkGetDoesNotExistFilm() throws Exception {
        Long filmNotExistId = 1L;
        mockMvc.perform(get(FILMS_ID, filmNotExistId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format(FILM_NOT_FOUND_MESSAGE, filmNotExistId)));
    }

    @Test
    void checkGettingOneFilmDB() throws Exception {
        Long filmId = getIdFromObject(createFilm(film));
        Optional<Film> optFilm = filmRepository.findById(filmId);
        Assertions.assertTrue(optFilm.isPresent());

        SoftAssertions softAssert = new SoftAssertions();
        checkFilm(optFilm.get(), film, softAssert);
        softAssert.assertAll();
    }

    @Test
    void checkGetDoesNotExistFilmDB() {
        Long filmNotExistId = 1L;
        Assertions.assertTrue(filmRepository.findById(filmNotExistId).isEmpty());
    }
}