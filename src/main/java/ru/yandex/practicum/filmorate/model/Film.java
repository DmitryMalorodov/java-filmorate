package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotation.NotBeforeDate;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.messages.FilmValidationMessages.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private Long id;
    @NotBlank(message = NAME_BLANK_MESSAGE)
    private String name;
    @Size(max = 200, message = DESCRIPTION_MAX_LENGTH_MESSAGE)
    private String description;
    @NotBeforeDate(value = RELEASE_DATE_MIN, message = RELEASE_DATE_MIN_MESSAGE)
    private LocalDate releaseDate;
    @Positive(message = DURATION_MUST_BE_POSITIVE_MESSAGE)
    private Integer duration;
}
