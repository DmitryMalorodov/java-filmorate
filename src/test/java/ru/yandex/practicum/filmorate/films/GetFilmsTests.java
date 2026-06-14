package ru.yandex.practicum.filmorate.films;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.endpoint.FilmEndpoints.FILMS;
import static ru.yandex.practicum.filmorate.films.FilmData.*;

@DisplayName("Проверка получения фильмов")
public class GetFilmsTests extends FilmTest {

    @Autowired
    public GetFilmsTests(FilmRepository filmRepository) {
        super(filmRepository);
    }

    @Test
    void checkGettingOneFilm() throws Exception {
        createFilm(film);

        mockMvc.perform(get(FILMS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value(film.getName()))
                .andExpect(jsonPath("$[0].description").value(film.getDescription()))
                .andExpect(jsonPath("$[0].releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("$[0].duration").value(film.getDuration()))
                .andExpect(jsonPath("$[0].mpaId").value(film.getMpaId()));
    }

    @Test
    void checkGettingTwoFilms() throws Exception {
        createFilm(film);
        createFilm(film);

        mockMvc.perform(get(FILMS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value(film.getName()))
                .andExpect(jsonPath("$[0].description").value(film.getDescription()))
                .andExpect(jsonPath("$[0].releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("$[0].duration").value(film.getDuration()))
                .andExpect(jsonPath("$[0].mpaId").value(film.getMpaId()))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].name").value(film.getName()))
                .andExpect(jsonPath("$[1].description").value(film.getDescription()))
                .andExpect(jsonPath("$[1].releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("$[1].duration").value(film.getDuration()))
                .andExpect(jsonPath("$[1].mpaId").value(film.getMpaId()));
    }

    @Test
    void checkGettingNoOneFilm() throws Exception {
        mockMvc.perform(get(FILMS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void checkGettingOneFilmDB() throws Exception {
        createFilm(film);
        List<Film> films = filmRepository.findAll();
        Assertions.assertEquals(1, films.size());

        SoftAssertions softAssert = new SoftAssertions();
        checkFilm(films.getFirst(), film, softAssert);
        softAssert.assertAll();
    }

    @Test
    void checkGettingNoOneFilmDB() {
        Assertions.assertTrue(filmRepository.findAll().isEmpty());
    }
}