package ru.yandex.practicum.filmorate.users;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.users.UserData.*;

@DisplayName("Проверка получения пользователей")
public class GetUsersTests extends UserTest {

    @Autowired
    public GetUsersTests(UserRepository userRepository) {
        super(userRepository);
    }

    @Test
    void checkGettingOneUser() throws Exception {
        createUser(user);

        mockMvc.perform(get(USERS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$[0].login").value(user.getLogin()))
                .andExpect(jsonPath("$[0].name").value(user.getName()))
                .andExpect(jsonPath("$[0].birthday").value(user.getBirthday().toString()));
    }

    @Test
    void checkGettingTwoUsers() throws Exception {
        createUser(user);
        createUser(user2);

        mockMvc.perform(get(USERS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$[0].login").value(user.getLogin()))
                .andExpect(jsonPath("$[0].name").value(user.getName()))
                .andExpect(jsonPath("$[0].birthday").value(user.getBirthday().toString()))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].email").value(user2.getEmail()))
                .andExpect(jsonPath("$[1].login").value(user2.getLogin()))
                .andExpect(jsonPath("$[1].name").value(user2.getName()))
                .andExpect(jsonPath("$[1].birthday").value(user2.getBirthday().toString()));
    }

    @Test
    void checkGettingNoOneUser() throws Exception {
        mockMvc.perform(get(USERS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void checkGettingOneUserDB() throws Exception {
        createUser(user);
        List<User> users = userRepository.findAll();
        Assertions.assertEquals(1, users.size());

        SoftAssertions softAssert = new SoftAssertions();
        checkUser(users.getFirst(), user, softAssert);
        softAssert.assertAll();
    }

    @Test
    void checkGettingTwoUsersDB() throws Exception {
        createUser(user);
        createUser(user2);
        List<User> users = userRepository.findAll();
        Assertions.assertEquals(2, users.size());

        SoftAssertions softAssert = new SoftAssertions();
        checkUser(users.getFirst(), user, softAssert);
        checkUser(users.getLast(), user2, softAssert);
        softAssert.assertAll();
    }

    @Test
    void checkGettingNoOneUserDB() {
        Assertions.assertTrue(userRepository.findAll().isEmpty());
    }
}
