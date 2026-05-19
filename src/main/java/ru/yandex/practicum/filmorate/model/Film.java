package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotation.NotBeforeDate;
import ru.yandex.practicum.filmorate.marker.OnCreate;
import ru.yandex.practicum.filmorate.marker.OnUpdate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static ru.yandex.practicum.filmorate.constant.message.FilmValidationMessages.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @NotNull(groups = OnUpdate.class, message = ID_NULL_MESSAGE)
    private Long id;

    @NotBlank(groups = OnCreate.class, message = NAME_BLANK_MESSAGE)
    private String name;

    @Size(groups = {OnCreate.class, OnUpdate.class}, max = 200, message = DESCRIPTION_MAX_LENGTH_MESSAGE)
    private String description;

    @NotBeforeDate(groups = {OnCreate.class, OnUpdate.class}, value = RELEASE_DATE_MIN, message = RELEASE_DATE_MIN_MESSAGE)
    private LocalDate releaseDate;

    @Positive(groups = {OnCreate.class, OnUpdate.class}, message = DURATION_MUST_BE_POSITIVE_MESSAGE)
    private Integer duration;

    @Builder.Default
    private Set<Long> likesUsersId = new HashSet<>(); // id юзеров, которые поставили лайк фильму
}
