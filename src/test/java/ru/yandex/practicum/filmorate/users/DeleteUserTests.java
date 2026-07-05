package ru.yandex.practicum.filmorate.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.message.UserValidationMessages.USER_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.users.UserData.user;

@DisplayName("Проверка удаления пользователя")
public class DeleteUserTests extends UserTest {

    @Autowired
    public DeleteUserTests(UserRepository userRepository) {
        super(userRepository);
    }

    @Test
    void checkDeleteUser() throws Exception {
        Long userId = getIdFromObject(createUser(user));

        mockMvc.perform(delete(USERS_ID, userId))
                .andExpect(status().isOk());

        mockMvc.perform(get(USERS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void checkDeleteUserWithDoesNotExistId() throws Exception {
        Long notExistFilmId = 10L;
        mockMvc.perform(delete(USERS_ID, notExistFilmId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format(USER_NOT_FOUND_MESSAGE, notExistFilmId)));
    }

    @Test
    void checkDeleteUserDB() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        userRepository.deleteUser(userId);
        Assertions.assertTrue(userRepository.findById(userId).isEmpty());
    }
}
