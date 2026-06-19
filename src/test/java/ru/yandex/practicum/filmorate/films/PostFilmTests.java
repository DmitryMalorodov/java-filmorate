package ru.yandex.practicum.filmorate.films;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.model.film.Film;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.message.FilmValidationMessages.*;
import static ru.yandex.practicum.filmorate.films.FilmData.*;

@DisplayName("Проверка добавления фильмов")
public class PostFilmTests extends FilmTest {

    @Autowired
    public PostFilmTests(FilmRepository filmRepository) {
        super(filmRepository);
    }

    @Test
    void checkCreateFilm() throws Exception {
        createFilm(film)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(film.getName()))
                .andExpect(jsonPath("$.description").value(film.getDescription()))
                .andExpect(jsonPath("$.releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("$.duration").value(film.getDuration()))
                .andExpect(jsonPath("$.mpa.id").value(film.getMpa().getId()))
                .andExpect(jsonPath("$.mpa.name").value(film.getMpa().getName()));
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

    @Test
    void checkCreateFilmDB() {
        Film actFilm = filmRepository.save(film);

        SoftAssertions softAssert = new SoftAssertions();
        checkFilm(actFilm, film, softAssert);
        softAssert.assertAll();
    }
}
