package ru.yandex.practicum.filmorate.films;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.time.LocalDate;
import java.util.Set;

public class FilmData {
    public static final Film film = Film.builder()
            .name("Имя фильма")
            .description("Описание")
            .releaseDate(LocalDate.of(2000, 12, 25))
            .duration(145)
            .mpa(new Mpa(1, "G"))
            .genres(Set.of(new Genre(1, "Комедия")))
            .build();

    static final Film film2 = Film.builder()
            .name("Имя фильма2")
            .description("Описание2")
            .releaseDate(LocalDate.of(2000, 12, 20))
            .duration(100)
            .mpa(new Mpa(4, "R"))
            .genres(Set.of(new Genre(6, "Боевик"), new Genre(2, "Драма")))
            .build();

    static final String STR_200_LENGTH = "Описание200символов!Описание200символов!" +
            "Описание200символов!Описание200символов!Описание200символов!Описание200символов!" +
            "Описание200символов!Описание200символов!Описание200символов!Описание200символов!";

    static final String STR_201_LENGTH = "Описание201символов!Описание201символов!" +
            "Описание201символов!Описание201символов!Описание201символов!Описание201символов!" +
            "Описание201символов!Описание201символов!Описание201символов!Описание201символов!!";

    static final LocalDate TOO_SOON_RELEASE_DATE = LocalDate.of(1895, 12, 27);
    static final LocalDate CORRECT_RELEASE_DATE = LocalDate.of(1895, 12, 28);
}
