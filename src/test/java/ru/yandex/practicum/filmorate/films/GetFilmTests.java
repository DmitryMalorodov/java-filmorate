package ru.yandex.practicum.filmorate.films;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.endpoint.FilmEndpoints.FILMS_ID;
import static ru.yandex.practicum.filmorate.constant.message.FilmValidationMessages.FILM_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.films.FilmData.film;

@DisplayName("Проверка получения фильма")
public class GetFilmTests extends FilmTest {

    @Test
    void checkGettingOneFilm() throws Exception {
        Long filmId = getIdFromObject(createFilm(film));

        mockMvc.perform(get(FILMS_ID, filmId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Имя фильма"))
                .andExpect(jsonPath("$.description").value("Описание"))
                .andExpect(jsonPath("$.releaseDate").value("2000-12-25"))
                .andExpect(jsonPath("$.duration").value(145));
    }

    @Test
    void checkGetDoesNotExistFilm() throws Exception {
        Long filmNotExistId = 1L;
        mockMvc.perform(get(FILMS_ID, filmNotExistId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format(FILM_NOT_FOUND_MESSAGE, filmNotExistId)));
    }
}
