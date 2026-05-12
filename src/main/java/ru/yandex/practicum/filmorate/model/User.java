package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.marker.OnCreate;
import ru.yandex.practicum.filmorate.marker.OnUpdate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static ru.yandex.practicum.filmorate.messages.UserValidationMessages.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotNull(groups = OnUpdate.class, message = ID_NULL_MESSAGE)
    private Long id;

    @NotBlank(groups = OnCreate.class, message = EMAIL_BLANK_MESSAGE)
    @Email(groups = {OnCreate.class, OnUpdate.class}, message = EMAIL_NOT_CORRECT_MESSAGE)
    private String email;

    @NotBlank(groups = OnCreate.class, message = LOGIN_BLANK_MESSAGE)
    private String login;

    private String name;

    @PastOrPresent(groups = {OnCreate.class, OnUpdate.class}, message = BIRTHDAY_COULD_NOT_BE_IN_FUTURE_MESSAGE)
    private LocalDate birthday;

    @Builder.Default
    private Set<Long> friendsIds = new HashSet<>();
}
