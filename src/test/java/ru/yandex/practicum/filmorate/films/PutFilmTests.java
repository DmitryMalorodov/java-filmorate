package ru.yandex.practicum.filmorate.films;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.messages.FilmValidationMessages.*;
import static ru.yandex.practicum.filmorate.messages.FilmValidationMessages.DURATION_MUST_BE_POSITIVE_MESSAGE;
import static ru.yandex.practicum.filmorate.films.FilmData.*;

@DisplayName("Проверка изменения фильмов")
public class PutFilmTests extends FilmTest {

    @Test
    void checkChangeFilm() throws Exception {
        Film newFilm = prepareReqBody(film2);

        changeFilm(newFilm)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(newFilm.getId()))
                .andExpect(jsonPath("$.name").value("Имя фильма 2"))
                .andExpect(jsonPath("$.description").value("Описание 2"))
                .andExpect(jsonPath("$.releaseDate").value("2020-01-03"))
                .andExpect(jsonPath("$.duration").value(200));
    }

    @Test
    void checkNameNullValidation() throws Exception {
        Film newFilm = prepareReqBody(film2);
        newFilm.setName(filmNameNull.getName());

        checkValidationError(changeFilm(newFilm), NAME_BLANK_MESSAGE);
    }

    @Test
    void checkNameBlankValidation() throws Exception {
        Film newFilm = prepareReqBody(film2);
        newFilm.setName(filmNameBlank.getName());

        checkValidationError(changeFilm(newFilm), NAME_BLANK_MESSAGE);
    }

    @Test
    void checkDescriptionNullValidation() throws Exception {
        Film newFilm = prepareReqBody(film2);
        newFilm.setDescription(filmDescriptionNull.getDescription());

        changeFilm(newFilm)
                .andExpect(status().isOk());
    }

    @Test
    void checkDescriptionBlankValidation() throws Exception {
        Film newFilm = prepareReqBody(film2);
        newFilm.setDescription(filmDescriptionBlank.getDescription());

        changeFilm(newFilm)
                .andExpect(status().isOk());
    }

    @Test
    void checkDescription200LengthValidation() throws Exception {
        Film newFilm = prepareReqBody(film2);
        newFilm.setDescription(filmDescription200Length.getDescription());

        changeFilm(newFilm)
                .andExpect(status().isOk());
    }

    @Test
    void checkDescription201LengthValidation() throws Exception {
        Film newFilm = prepareReqBody(film2);
        newFilm.setDescription(filmDescription201Length.getDescription());

        checkValidationError(changeFilm(newFilm), DESCRIPTION_MAX_LENGTH_MESSAGE);
    }

    @Test
    void checkReleaseDateTooSoonValidation() throws Exception {
        Film newFilm = prepareReqBody(film2);
        newFilm.setReleaseDate(filmWithReleaseTooSoon.getReleaseDate());

        checkValidationError(changeFilm(newFilm), RELEASE_DATE_MIN_MESSAGE);
    }

    @Test
    void checkReleaseDateCorrectValidation() throws Exception {
        Film newFilm = prepareReqBody(film2);
        newFilm.setReleaseDate(filmWithReleaseCorrect.getReleaseDate());

        changeFilm(newFilm)
                .andExpect(status().isOk());
    }

    @Test
    void checkDurationNegativeValidation() throws Exception {
        Film newFilm = prepareReqBody(film2);
        newFilm.setDuration(filmWithDurationNegative.getDuration());

        checkValidationError(changeFilm(newFilm), DURATION_MUST_BE_POSITIVE_MESSAGE);
    }

    @Test
    void checkDurationZeroValidation() throws Exception {
        Film newFilm = prepareReqBody(film2);
        newFilm.setDuration(filmWithDurationZero.getDuration());

        checkValidationError(changeFilm(newFilm), DURATION_MUST_BE_POSITIVE_MESSAGE);
    }

    @Test
    void checkDurationPositiveValidation() throws Exception {
        Film newFilm = prepareReqBody(film2);
        newFilm.setDuration(filmWithDurationPositive.getDuration());

        changeFilm(newFilm)
                .andExpect(status().isOk());
    }

    private Film prepareReqBody(Film film) throws Exception {
        return film.toBuilder()
                .id(getIdFromObject(createFilm(film)))
                .build();
    }
}
