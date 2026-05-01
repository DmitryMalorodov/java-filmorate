package ru.yandex.practicum.filmorate.films;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.messages.FilmValidationMessages.*;

@DisplayName("Проверка добавления фильмов")
public class PostFilmTests extends FilmTest {

    @Test
    void checkCreateFilm() throws Exception {
        createFilm(film)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Имя фильма"))
                .andExpect(jsonPath("$.description").value("Описание"))
                .andExpect(jsonPath("$.releaseDate").value("2000-12-25"))
                .andExpect(jsonPath("$.duration").value(145));
    }

    @Test
    void checkNameNullValidation() throws Exception {
        checkValidationError(createFilm(filmNameNull), NAME_BLANK_MESSAGE);
    }

    @Test
    void checkNameBlankValidation() throws Exception {
        checkValidationError(createFilm(filmNameBlank), NAME_BLANK_MESSAGE);
    }

    @Test
    void checkDescriptionNullValidation() throws Exception {
        createFilm(filmDescriptionNull)
                .andExpect(status().isOk());
    }

    @Test
    void checkDescriptionBlankValidation() throws Exception {
        createFilm(filmDescriptionBlank)
                .andExpect(status().isOk());
    }

    @Test
    void checkDescription200LengthValidation() throws Exception {
        createFilm(filmDescription200Length)
                .andExpect(status().isOk());
    }

    @Test
    void checkDescription201LengthValidation() throws Exception {
        checkValidationError(createFilm(filmDescription201Length), DESCRIPTION_MAX_LENGTH_MESSAGE);
    }

    @Test
    void checkReleaseDateTooSoonValidation() throws Exception {
        checkValidationError(createFilm(filmWithReleaseTooSoon), RELEASE_DATE_MIN_MESSAGE);
    }

    @Test
    void checkReleaseDateCorrectValidation() throws Exception {
        createFilm(filmWithReleaseCorrect)
                .andExpect(status().isOk());
    }

    @Test
    void checkDurationNegativeValidation() throws Exception {
        checkValidationError(createFilm(filmWithDurationNegative), DURATION_MUST_BE_POSITIVE_MESSAGE);
    }

    @Test
    void checkDurationZeroValidation() throws Exception {
        checkValidationError(createFilm(filmWithDurationZero), DURATION_MUST_BE_POSITIVE_MESSAGE);
    }

    @Test
    void checkDurationPositiveValidation() throws Exception {
        createFilm(filmWithDurationPositive)
                .andExpect(status().isOk());
    }
}
