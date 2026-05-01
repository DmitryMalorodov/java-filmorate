package ru.yandex.practicum.filmorate.films;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.messages.FilmValidationMessages.*;
import static ru.yandex.practicum.filmorate.messages.FilmValidationMessages.DURATION_MUST_BE_POSITIVE_MESSAGE;

@DisplayName("Проверка изменения фильмов")
public class PutFilmTests extends FilmTest {

    @Test
    void checkChangeFilm() throws Exception {
        Film newFilm = prepareReqBody();

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
        Film newFilm = prepareReqBody();
        newFilm.setName(filmNameNull.getName());

        checkValidationError(changeFilm(newFilm), NAME_BLANK_MESSAGE);
    }

    @Test
    void checkNameBlankValidation() throws Exception {
        Film newFilm = prepareReqBody();
        newFilm.setName(filmNameBlank.getName());

        checkValidationError(changeFilm(newFilm), NAME_BLANK_MESSAGE);
    }

    @Test
    void checkDescriptionNullValidation() throws Exception {
        Film newFilm = prepareReqBody();
        newFilm.setDescription(filmDescriptionNull.getDescription());

        changeFilm(newFilm)
                .andExpect(status().isOk());
    }

    @Test
    void checkDescriptionBlankValidation() throws Exception {
        Film newFilm = prepareReqBody();
        newFilm.setDescription(filmDescriptionBlank.getDescription());

        changeFilm(newFilm)
                .andExpect(status().isOk());
    }

    @Test
    void checkDescription200LengthValidation() throws Exception {
        Film newFilm = prepareReqBody();
        newFilm.setDescription(filmDescription200Length.getDescription());

        changeFilm(newFilm)
                .andExpect(status().isOk());
    }

    @Test
    void checkDescription201LengthValidation() throws Exception {
        Film newFilm = prepareReqBody();
        newFilm.setDescription(filmDescription201Length.getDescription());

        checkValidationError(changeFilm(newFilm), DESCRIPTION_MAX_LENGTH_MESSAGE);
    }

    @Test
    void checkReleaseDateTooSoonValidation() throws Exception {
        Film newFilm = prepareReqBody();
        newFilm.setReleaseDate(filmWithReleaseTooSoon.getReleaseDate());

        checkValidationError(changeFilm(newFilm), RELEASE_DATE_MIN_MESSAGE);
    }

    @Test
    void checkReleaseDateCorrectValidation() throws Exception {
        Film newFilm = prepareReqBody();
        newFilm.setReleaseDate(filmWithReleaseCorrect.getReleaseDate());

        changeFilm(newFilm)
                .andExpect(status().isOk());
    }

    @Test
    void checkDurationNegativeValidation() throws Exception {
        Film newFilm = prepareReqBody();
        newFilm.setDuration(filmWithDurationNegative.getDuration());

        checkValidationError(changeFilm(newFilm), DURATION_MUST_BE_POSITIVE_MESSAGE);
    }

    @Test
    void checkDurationZeroValidation() throws Exception {
        Film newFilm = prepareReqBody();
        newFilm.setDuration(filmWithDurationZero.getDuration());

        checkValidationError(changeFilm(newFilm), DURATION_MUST_BE_POSITIVE_MESSAGE);
    }

    @Test
    void checkDurationPositiveValidation() throws Exception {
        Film newFilm = prepareReqBody();
        newFilm.setDuration(filmWithDurationPositive.getDuration());

        changeFilm(newFilm)
                .andExpect(status().isOk());
    }

    private Long createFilmAndGetId() throws Exception {
        return JsonPath.parse(createFilm(film)
                .andReturn()
                .getResponse()
                .getContentAsString()).read("$.id", Long.class);
    }

    private Film prepareReqBody() throws Exception {
        return film2.toBuilder()
                .id(createFilmAndGetId())
                .build();
    }
}
