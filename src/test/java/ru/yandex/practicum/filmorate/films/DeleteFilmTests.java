package ru.yandex.practicum.filmorate.films;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.FilmRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.message.FilmValidationMessages.FILM_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.films.FilmData.film;

@DisplayName("Проверка удаления фильма")
public class DeleteFilmTests extends FilmTest {

    @Autowired
    public DeleteFilmTests(FilmRepository filmRepository) {
        super(filmRepository);
    }

    @Test
    void checkDeleteFilm() throws Exception {
        Long filmId = getIdFromObject(createFilm(film));

        mockMvc.perform(delete(FILMS_ID, filmId))
                .andExpect(status().isOk());

        mockMvc.perform(get(FILMS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void checkDeleteFilmWithDoesNotExistId() throws Exception {
        Long notExistFilmId = 10L;
        mockMvc.perform(delete(FILMS_ID, notExistFilmId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format(FILM_NOT_FOUND_MESSAGE, notExistFilmId)));
    }

    @Test
    void checkDeleteFilmDB() throws Exception {
        Long filmId = getIdFromObject(createFilm(film));
        filmRepository.deleteFilm(filmId);
        Assertions.assertTrue(filmRepository.findById(filmId).isEmpty());
    }
}
