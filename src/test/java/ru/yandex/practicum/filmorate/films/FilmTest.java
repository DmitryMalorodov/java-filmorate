package ru.yandex.practicum.filmorate.films;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.yandex.practicum.filmorate.MainTest;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.model.film.Film;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static ru.yandex.practicum.filmorate.GeneralAssertions.isEqual;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmTest extends MainTest {
    static final String FILMS = "/films";
    static final String FILMS_ID = "/films/{id}";
    public static final String FILMS_ID_LIKE_USER_ID = "/films/{id}/like/{userId}";
    static final String FILMS_POPULAR = "/films/popular";
    static final String COMMON_FILMS = "/films/common";
    static final String FILMS_SEARCH = "/films/search";

    final FilmRepository filmRepository;

    ResultActions changeFilm(Film film) throws Exception {
        return mockMvc.perform(put(FILMS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(film)));
    }

    void checkFilm(Film actFilm, Film expFilm, SoftAssertions softAssert) {
        isEqual(actFilm.getName(), expFilm.getName(), "name '%s' отличается от ожидаемого '%s'", softAssert);
        isEqual(actFilm.getDescription(), expFilm.getDescription(), "description '%s' отличается от ожидаемого '%s'", softAssert);
        isEqual(actFilm.getReleaseDate(), expFilm.getReleaseDate(), "releaseDate '%s' отличается от ожидаемого '%s'", softAssert);
        isEqual(actFilm.getDuration(), expFilm.getDuration(), "duration '%s' отличается от ожидаемого '%s'", softAssert);
        isEqual(actFilm.getMpa(), expFilm.getMpa(), "mpaId '%s' отличается от ожидаемого '%s'", softAssert);
    }
}
