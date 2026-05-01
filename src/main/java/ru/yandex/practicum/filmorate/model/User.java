package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.messages.UserValidationMessages.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    @NotBlank(message = EMAIL_BLANK_MESSAGE)
    @Email(message = EMAIL_NOT_CORRECT_MESSAGE)
    private String email;
    @NotBlank(message = LOGIN_BLANK_MESSAGE)
    private String login;
    private String name;
    @PastOrPresent(message = BIRTHDAY_COULD_NOT_BE_IN_FUTURE_MESSAGE)
    private LocalDate birthday;
}
