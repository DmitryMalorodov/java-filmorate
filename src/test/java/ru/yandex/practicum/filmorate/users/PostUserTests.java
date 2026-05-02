package ru.yandex.practicum.filmorate.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.messages.UserValidationMessages.*;
import static ru.yandex.practicum.filmorate.users.UserData.*;

@DisplayName("Проверка добавления пользователя")
public class PostUserTests extends UserTest {

    @Test
    void checkCreateFilm() throws Exception {
        createUser(user)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("email@em.ru"))
                .andExpect(jsonPath("$.login").value("Логин"))
                .andExpect(jsonPath("$.name").value("Имя"))
                .andExpect(jsonPath("$.birthday").value("2000-10-20"));
    }

    @Test
    void checkEmailNullValidation() throws Exception {
        User userEmailNull = user.toBuilder().email(null).build();
        checkValidationError(createUser(userEmailNull), EMAIL_BLANK_MESSAGE);
    }

    @Test
    void checkEmailNotCorrectValidation() throws Exception {
        User userEmailNotCorrect = user.toBuilder().email("email").build();
        checkValidationError(createUser(userEmailNotCorrect), EMAIL_NOT_CORRECT_MESSAGE);
    }

    @Test
    void checkLoginNullValidation() throws Exception {
        User userLoginNull = user.toBuilder().login(null).build();
        checkValidationError(createUser(userLoginNull), LOGIN_BLANK_MESSAGE);
    }

    @Test
    void checkLoginBlankValidation() throws Exception {
        User userLoginBlank = user.toBuilder().login(" ").build();
        checkValidationError(createUser(userLoginBlank), LOGIN_BLANK_MESSAGE);
    }

    @Test
    void checkNameNullValidation() throws Exception {
        User userNameNull = user.toBuilder().name(null).build();
        createUser(userNameNull)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(userNameNull.getLogin()));
    }

    @Test
    void checkNameBlankValidation() throws Exception {
        User userNameBlank = user.toBuilder().name(" ").build();
        createUser(userNameBlank)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(userNameBlank.getLogin()));
    }

    @Test
    void checkBirthdayNowValidation() throws Exception {
        User userBirthdayNow = user.toBuilder().birthday(LocalDate.now()).build();
        createUser(userBirthdayNow)
                .andExpect(status().isOk());
    }

    @Test
    void checkBirthdayFutureValidation() throws Exception {
        User userBirthdayFuture = user.toBuilder().birthday(LocalDate.now().plusDays(1)).build();
        checkValidationError(createUser(userBirthdayFuture), BIRTHDAY_COULD_NOT_BE_IN_FUTURE_MESSAGE);
    }
}
