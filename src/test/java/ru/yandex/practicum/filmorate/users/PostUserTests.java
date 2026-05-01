package ru.yandex.practicum.filmorate.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        checkValidationError(createUser(userEmailNull), EMAIL_BLANK_MESSAGE);
    }

    @Test
    void checkEmailNotCorrectValidation() throws Exception {
        checkValidationError(createUser(userEmailNotCorrect), EMAIL_NOT_CORRECT_MESSAGE);
    }

    @Test
    void checkLoginNullValidation() throws Exception {
        checkValidationError(createUser(userLoginNull), LOGIN_BLANK_MESSAGE);
    }

    @Test
    void checkLoginBlankValidation() throws Exception {
        checkValidationError(createUser(userLoginBlank), LOGIN_BLANK_MESSAGE);
    }

    @Test
    void checkNameNullValidation() throws Exception {
        createUser(userNameNull)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(userNameNull.getLogin()));
    }

    @Test
    void checkNameBlankValidation() throws Exception {
        createUser(userNameBlank)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(userNameBlank.getLogin()));
    }

    @Test
    void checkBirthdayNowValidation() throws Exception {
        createUser(userBirthdayNow)
                .andExpect(status().isOk());
    }

    @Test
    void checkBirthdayFutureValidation() throws Exception {
        checkValidationError(createUser(userBirthdayFuture), BIRTHDAY_COULD_NOT_BE_IN_FUTURE_MESSAGE);
    }
}
