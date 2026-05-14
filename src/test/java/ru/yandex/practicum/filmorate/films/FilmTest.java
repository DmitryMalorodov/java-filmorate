package ru.yandex.practicum.filmorate.films;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.yandex.practicum.filmorate.MainTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static ru.yandex.practicum.filmorate.constant.endpoint.FilmEndpoints.FILMS;

@WebMvcTest({FilmController.class, FilmService.class, UserController.class, UserService.class, UserStorage.class, InMemoryFilmStorage.class})
public class FilmTest extends MainTest {

    ResultActions createFilm(Film film) throws Exception {
        return mockMvc.perform(post(FILMS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(film)));
    }

    ResultActions changeFilm(Film film) throws Exception {
        return mockMvc.perform(put(FILMS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(film)));
    }
}
