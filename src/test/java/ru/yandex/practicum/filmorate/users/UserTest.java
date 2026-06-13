package ru.yandex.practicum.filmorate.users;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.yandex.practicum.filmorate.MainTest;

import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.user.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static ru.yandex.practicum.filmorate.GeneralAssertions.isEqual;
import static ru.yandex.practicum.filmorate.constant.endpoint.UserEndpoints.USERS;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserTest extends MainTest {
    final UserRepository userRepository;

    ResultActions changeUser(User user) throws Exception {
        return mockMvc.perform(put(USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));
    }

    void checkUser(User actUser, User expUser, SoftAssertions softAssert) {
        isEqual(actUser.getEmail(), expUser.getEmail(), "email '%s' отличается от ожидаемого '%s'", softAssert);
        isEqual(actUser.getLogin(), expUser.getLogin(), "login '%s' отличается от ожидаемого '%s'", softAssert);
        isEqual(actUser.getName(), expUser.getName(), "name '%s' отличается от ожидаемого '%s'", softAssert);
        isEqual(actUser.getBirthday(), expUser.getBirthday(), "birthday '%s' отличается от ожидаемого '%s'", softAssert);
    }
}
