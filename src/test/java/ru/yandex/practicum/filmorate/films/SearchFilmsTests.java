package ru.yandex.practicum.filmorate.films;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.FilmRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.films.FilmData.film;
import static ru.yandex.practicum.filmorate.films.FilmData.film2;

@DisplayName("Проверка поиска по названию фильма и режисерам")
public class SearchFilmsTests extends FilmTest {
    @Autowired
    SearchFilmsTests(FilmRepository filmRepository) {
        super(filmRepository);
    }

    @Test
    public void searchExistingFilmByTitle() throws Exception {
        createFilm(film);
        createFilm(film2);

        mockMvc.perform(get(FILMS_SEARCH).param("query", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    public void searchNotExistingFilmByTitle() throws Exception {
        createFilm(film);

        mockMvc.perform(get(FILMS_SEARCH).param("query", "триц"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

    }

    @Test
    public void searchWhenQueryIsEmpty() throws Exception {
        createFilm(film);
        createFilm(film);
        createFilm(film);
        //вернет полный список фильмов при поиске с пустым запросом
        mockMvc.perform(get(FILMS_SEARCH).param("query", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Имя фильма"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3));
    }

    @Test
    public void searchFilmsByDirector() throws Exception {
        createFilm(film);
        createFilm(film2);

        mockMvc.perform(get(FILMS_SEARCH).param("query", "нол").param("by", "director", "title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(2));


    }

    @Test
    public void searchWithEmptyQueryAllParams() throws Exception {
        createFilm(film);
        createFilm(film2);
        //вернет полный список фильмов при поиске с пустым запросом
        mockMvc.perform(get(FILMS_SEARCH).param("query", "").param("by", "directors", "title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].id").value(2));


    }
}
