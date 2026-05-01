package ru.yandex.practicum.filmorate.films;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmData {
    static final Film film = Film.builder()
            .name("Имя фильма")
            .description("Описание")
            .releaseDate(LocalDate.of(2000, 12, 25))
            .duration(145)
            .build();

    static final Film film2 = Film.builder()
            .name("Имя фильма 2")
            .description("Описание 2")
            .releaseDate(LocalDate.of(2020, 1, 3))
            .duration(200)
            .build();

    static final Film filmNameNull = Film.builder()
            .name(null)
            .description("Описание 2")
            .releaseDate(LocalDate.of(2020, 1, 3))
            .duration(200)
            .build();

    static final Film filmNameBlank = Film.builder()
            .name(" ")
            .description("Описание 2")
            .releaseDate(LocalDate.of(2020, 1, 3))
            .duration(200)
            .build();

    static final Film filmDescriptionNull = Film.builder()
            .name("Имя фильма")
            .description(null)
            .releaseDate(LocalDate.of(2020, 1, 3))
            .duration(200)
            .build();

    static final Film filmDescriptionBlank = Film.builder()
            .name("Имя фильма")
            .description(" ")
            .releaseDate(LocalDate.of(2020, 1, 3))
            .duration(200)
            .build();

    static final Film filmDescription200Length = Film.builder()
            .name("Имя фильма")
            .description("Описание200символов!Описание200символов!Описание200символов!Описание200символов!Описание200символов!" +
                    "Описание200символов!Описание200символов!Описание200символов!Описание200символов!Описание200символов!")
            .releaseDate(LocalDate.of(2020, 1, 3))
            .duration(200)
            .build();

    static final Film filmDescription201Length = Film.builder()
            .name("Имя фильма")
            .description("Описание201символов!Описание201символов!Описание200символов!Описание201символов!Описание201символов!" +
                    "Описание201символов!Описание201символов!Описание201символов!Описание201символов!Описание201символов!!")
            .releaseDate(LocalDate.of(2020, 1, 3))
            .duration(200)
            .build();

    static final Film filmWithReleaseTooSoon = Film.builder()
            .name("Имя фильма")
            .description("Описание")
            .releaseDate(LocalDate.of(1895, 12, 27))
            .duration(145)
            .build();

    static final Film filmWithReleaseCorrect = Film.builder()
            .name("Имя фильма")
            .description("Описание")
            .releaseDate(LocalDate.of(1895, 12, 28))
            .duration(145)
            .build();

    static final Film filmWithDurationNegative = Film.builder()
            .name("Имя фильма")
            .description("Описание")
            .releaseDate(LocalDate.of(1900, 12, 28))
            .duration(-1)
            .build();

    static final Film filmWithDurationZero = Film.builder()
            .name("Имя фильма")
            .description("Описание")
            .releaseDate(LocalDate.of(1900, 12, 28))
            .duration(0)
            .build();

    static final Film filmWithDurationPositive = Film.builder()
            .name("Имя фильма")
            .description("Описание")
            .releaseDate(LocalDate.of(1900, 12, 28))
            .duration(1)
            .build();
}
