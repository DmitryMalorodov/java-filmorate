package ru.yandex.practicum.filmorate.users;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.message.UserValidationMessages.USER_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.users.UserData.user;

@DisplayName("Проверка получения пользователя")
public class GetUserTests extends UserTest {

    @Autowired
    public GetUserTests(UserRepository userRepository) {
        super(userRepository);
    }

    @Test
    void checkGetUser() throws Exception {
        Long userId = getIdFromObject(createUser(user));

        mockMvc.perform(get(USERS_ID, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.login").value(user.getLogin()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.birthday").value(user.getBirthday().toString()));
    }

    @Test
    void checkGetDoesNotExistUser() throws Exception {
        Long userNotExistId = 10L;

        mockMvc.perform(get(USERS_ID, userNotExistId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format(USER_NOT_FOUND_MESSAGE, userNotExistId)));
    }

    @Test
    void checkGetUserDB() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Optional<User> optUser = userRepository.findById(userId);
        Assertions.assertTrue(optUser.isPresent());

        SoftAssertions softAssert = new SoftAssertions();
        checkUser(optUser.get(), user, softAssert);
        softAssert.assertAll();
    }

    @Test
    void checkGetDoesNotExistUserDB() {
        Long userNotExistId = 10L;
        Optional<User> user = userRepository.findById(userNotExistId);
        Assertions.assertTrue(user.isEmpty());
    }
}