package ru.yandex.practicum.filmorate.films;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.FilmRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.films.FilmData.film;
import static ru.yandex.practicum.filmorate.films.FilmData.film2;

public class SearchFilmsTests extends FilmTest{
    @Autowired
    SearchFilmsTests(FilmRepository filmRepository){
        super(filmRepository);
    }
    @Test
    public void searchExistingFilmByTitle() throws Exception {
        createFilm(film);
        createFilm(film2);

        mockMvc.perform(get(FILMS_SEARCH).param("query","2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    public void searchNotExistingFilmByTitle() throws Exception {
        createFilm(film);

        mockMvc.perform(get(FILMS_SEARCH).param("query","триц"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

    }

    @Test
    public void searchWhenQueryIsEmpty() throws Exception {
        createFilm(film);
        createFilm(film);
        createFilm(film);

        mockMvc.perform(get(FILMS_SEARCH).param("query",""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Имя фильма"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3));
    }

    @Test
    public void searchFilmsByDirector() throws Exception {
        createFilm(film);

        mockMvc.perform(get(FILMS_SEARCH).param("query",""))
                .andExpect(status().isOk());
        //но пока тут будет ошибка
    }
}
