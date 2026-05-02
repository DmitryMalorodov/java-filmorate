package ru.yandex.practicum.filmorate.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.messages.UserValidationMessages.*;
import static ru.yandex.practicum.filmorate.users.UserData.*;

@DisplayName("Проверка изменения пользователя")
public class PutUserTests extends UserTest {

    @Test
    void checkChangeUser() throws Exception {
        User newUser = prepareReqBody(user);

        changeUser(newUser)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("email@em.ru"))
                .andExpect(jsonPath("$.login").value("Логин"))
                .andExpect(jsonPath("$.name").value("Имя"))
                .andExpect(jsonPath("$.birthday").value("2000-10-20"));
    }

    @Test
    void checkEmailNullValidation() throws Exception {
        User newUser = prepareReqBody(user);
        newUser.setEmail(null);

        checkValidationError(changeUser(newUser), EMAIL_BLANK_MESSAGE);
    }

    @Test
    void checkEmailNotCorrectValidation() throws Exception {
        User newUser = prepareReqBody(user);
        newUser.setEmail("email");

        checkValidationError(changeUser(newUser), EMAIL_NOT_CORRECT_MESSAGE);
    }

    @Test
    void checkLoginNullValidation() throws Exception {
        User newUser = prepareReqBody(user);
        newUser.setLogin(null);

        checkValidationError(changeUser(newUser), LOGIN_BLANK_MESSAGE);
    }

    @Test
    void checkLoginBlankValidation() throws Exception {
        User newUser = prepareReqBody(user);
        newUser.setLogin(" ");

        checkValidationError(changeUser(newUser), LOGIN_BLANK_MESSAGE);
    }

    @Test
    void checkNameNullValidation() throws Exception {
        User newUser = prepareReqBody(user);
        newUser.setName(null);

        changeUser(newUser)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getLogin()));
    }

    @Test
    void checkNameBlankValidation() throws Exception {
        User newUser = prepareReqBody(user);
        newUser.setName(" ");

        changeUser(newUser)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getLogin()));
    }

    @Test
    void checkBirthdayNowValidation() throws Exception {
        User newUser = prepareReqBody(user);
        newUser.setBirthday(LocalDate.now());

        changeUser(newUser)
                .andExpect(status().isOk());
    }

    @Test
    void checkBirthdayFutureValidation() throws Exception {
        User newUser = prepareReqBody(user);
        newUser.setBirthday(LocalDate.now().plusDays(1));

        checkValidationError(changeUser(newUser), BIRTHDAY_COULD_NOT_BE_IN_FUTURE_MESSAGE);
    }

    private User prepareReqBody(User user) throws Exception {
        return user.toBuilder()
                .id(getIdFromObject(createUser(user)))
                .build();
    }
}
