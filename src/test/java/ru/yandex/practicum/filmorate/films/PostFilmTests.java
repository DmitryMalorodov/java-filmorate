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
        createFilm(filmNameNull)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(NAME_BLANK_MESSAGE));
    }

    @Test
    void checkNameBlankValidation() throws Exception {
        createFilm(filmNameBlank)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(NAME_BLANK_MESSAGE));
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
        createFilm(filmDescription201Length)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(DESCRIPTION_MAX_LENGTH_MESSAGE));
    }

    @Test
    void checkReleaseDateTooSoonValidation() throws Exception {
        createFilm(filmWithReleaseTooSoon)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(RELEASE_DATE_MIN_MESSAGE));
    }

    @Test
    void checkReleaseDateCorrectValidation() throws Exception {
        createFilm(filmWithReleaseCorrect)
                .andExpect(status().isOk());
    }

    @Test
    void checkDurationNegativeValidation() throws Exception {
        createFilm(filmWithDurationNegative)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(DURATION_MUST_BE_POSITIVE_MESSAGE));
    }

    @Test
    void checkDurationZeroValidation() throws Exception {
        createFilm(filmWithDurationZero)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(DURATION_MUST_BE_POSITIVE_MESSAGE));
    }

    @Test
    void checkDurationPositiveValidation() throws Exception {
        createFilm(filmWithDurationPositive)
                .andExpect(status().isOk());
    }
}
