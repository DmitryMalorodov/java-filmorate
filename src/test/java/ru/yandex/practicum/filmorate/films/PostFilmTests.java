package ru.yandex.practicum.filmorate.films;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.message.FilmValidationMessages.*;
import static ru.yandex.practicum.filmorate.films.FilmData.*;

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
        Film filmNameNull = film.toBuilder().name(null).build();
        checkValidationError(createFilm(filmNameNull), NAME_BLANK_MESSAGE);
    }

    @Test
    void checkNameBlankValidation() throws Exception {
        Film filmNameBlank = film.toBuilder().name(" ").build();
        checkValidationError(createFilm(filmNameBlank), NAME_BLANK_MESSAGE);
    }

    @Test
    void checkDescriptionNullValidation() throws Exception {
        Film filmDescriptionNull = film.toBuilder().description(null).build();
        createFilm(filmDescriptionNull)
                .andExpect(status().isOk());
    }

    @Test
    void checkDescriptionBlankValidation() throws Exception {
        Film filmDescriptionBlank = film.toBuilder().description(" ").build();
        createFilm(filmDescriptionBlank)
                .andExpect(status().isOk());
    }

    @Test
    void checkDescription200LengthValidation() throws Exception {
        Film filmDescription200Length = film.toBuilder().description(STR_200_LENGTH).build();
        createFilm(filmDescription200Length)
                .andExpect(status().isOk());
    }

    @Test
    void checkDescription201LengthValidation() throws Exception {
        Film filmDescription201Length = film.toBuilder().description(STR_201_LENGTH).build();
        checkValidationError(createFilm(filmDescription201Length), DESCRIPTION_MAX_LENGTH_MESSAGE);
    }

    @Test
    void checkReleaseDateTooSoonValidation() throws Exception {
        Film filmWithReleaseTooSoon = film.toBuilder().releaseDate(TOO_SOON_RELEASE_DATE).build();
        checkValidationError(createFilm(filmWithReleaseTooSoon), RELEASE_DATE_MIN_MESSAGE);
    }

    @Test
    void checkReleaseDateCorrectValidation() throws Exception {
        Film filmWithReleaseCorrect = film.toBuilder().releaseDate(CORRECT_RELEASE_DATE).build();
        createFilm(filmWithReleaseCorrect)
                .andExpect(status().isOk());
    }

    @Test
    void checkDurationNegativeValidation() throws Exception {
        Film filmWithDurationNegative = film.toBuilder().duration(-1).build();
        checkValidationError(createFilm(filmWithDurationNegative), DURATION_MUST_BE_POSITIVE_MESSAGE);
    }

    @Test
    void checkDurationZeroValidation() throws Exception {
        Film filmWithDurationZero = film.toBuilder().duration(0).build();
        checkValidationError(createFilm(filmWithDurationZero), DURATION_MUST_BE_POSITIVE_MESSAGE);
    }

    @Test
    void checkDurationPositiveValidation() throws Exception {
        Film filmWithDurationPositive = film.toBuilder().duration(1).build();
        createFilm(filmWithDurationPositive)
                .andExpect(status().isOk());
    }

    @Test
    void checkIdNullValidation() throws Exception {
        Film filmWithoutId = film.toBuilder().id(null).build();
        createFilm(filmWithoutId)
                .andExpect(status().isOk());
    }
}
