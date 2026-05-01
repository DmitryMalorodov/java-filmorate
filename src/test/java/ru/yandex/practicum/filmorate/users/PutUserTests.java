package ru.yandex.practicum.filmorate.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.messages.UserValidationMessages.*;
import static ru.yandex.practicum.filmorate.users.UserData.*;

@DisplayName("Проверка изменения пользователя")
public class PutUserTests extends UserTest {

    @Test
    void checkChangeUser() throws Exception {
        User newUser = prepareReqBody(user2);

        changeUser(newUser)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("email@em2.ru"))
                .andExpect(jsonPath("$.login").value("Логин 2"))
                .andExpect(jsonPath("$.name").value("Имя 2"))
                .andExpect(jsonPath("$.birthday").value("1990-10-20"));
    }

    @Test
    void checkEmailNullValidation() throws Exception {
        User newUser = prepareReqBody(user2);
        newUser.setEmail(userEmailNull.getEmail());

        checkValidationError(changeUser(newUser), EMAIL_BLANK_MESSAGE);
    }

    @Test
    void checkEmailNotCorrectValidation() throws Exception {
        User newUser = prepareReqBody(user2);
        newUser.setEmail(userEmailNotCorrect.getEmail());

        checkValidationError(changeUser(newUser), EMAIL_NOT_CORRECT_MESSAGE);
    }

    @Test
    void checkLoginNullValidation() throws Exception {
        User newUser = prepareReqBody(user2);
        newUser.setLogin(userLoginNull.getLogin());

        checkValidationError(changeUser(newUser), LOGIN_BLANK_MESSAGE);
    }

    @Test
    void checkLoginBlankValidation() throws Exception {
        User newUser = prepareReqBody(user2);
        newUser.setLogin(userLoginBlank.getLogin());

        checkValidationError(changeUser(newUser), LOGIN_BLANK_MESSAGE);
    }

    @Test
    void checkNameNullValidation() throws Exception {
        User newUser = prepareReqBody(user2);
        newUser.setName(userNameNull.getName());

        changeUser(newUser)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user2.getLogin()));
    }

    @Test
    void checkNameBlankValidation() throws Exception {
        User newUser = prepareReqBody(user2);
        newUser.setName(userNameBlank.getName());

        changeUser(newUser)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user2.getLogin()));
    }

    @Test
    void checkBirthdayNowValidation() throws Exception {
        User newUser = prepareReqBody(user2);
        newUser.setBirthday(userBirthdayNow.getBirthday());

        changeUser(newUser)
                .andExpect(status().isOk());
    }

    @Test
    void checkBirthdayFutureValidation() throws Exception {
        User newUser = prepareReqBody(user2);
        newUser.setBirthday(userBirthdayFuture.getBirthday());

        checkValidationError(changeUser(newUser), BIRTHDAY_COULD_NOT_BE_IN_FUTURE_MESSAGE);
    }

    private User prepareReqBody(User user) throws Exception {
        return user.toBuilder()
                .id(getIdFromObject(createUser(user)))
                .build();
    }
}
