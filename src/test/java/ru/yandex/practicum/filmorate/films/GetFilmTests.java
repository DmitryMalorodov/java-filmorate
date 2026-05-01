package ru.yandex.practicum.filmorate.films;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка получения фильмов")
public class GetFilmTests extends FilmTest {

    @Test
    void checkGettingOneFilm() throws Exception {
        createFilm(film);

        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("Имя фильма"))
                .andExpect(jsonPath("$[0].description").value("Описание"))
                .andExpect(jsonPath("$[0].releaseDate").value("2000-12-25"))
                .andExpect(jsonPath("$[0].duration").value(145));
    }

    @Test
    void checkGettingTwoFilms() throws Exception {
        createFilm(film);
        createFilm(film2);

        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("Имя фильма"))
                .andExpect(jsonPath("$[0].description").value("Описание"))
                .andExpect(jsonPath("$[0].releaseDate").value("2000-12-25"))
                .andExpect(jsonPath("$[0].duration").value(145))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].name").value("Имя фильма 2"))
                .andExpect(jsonPath("$[1].description").value("Описание 2"))
                .andExpect(jsonPath("$[1].releaseDate").value("2020-01-03"))
                .andExpect(jsonPath("$[1].duration").value(200));
    }

    @Test
    void checkGettingNoOneFilm() throws Exception {
        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
