package ru.yandex.practicum.filmorate.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.users.UserData.*;

@DisplayName("Проверка получения пользователей")
public class GetUserTests extends UserTest {

    @Test
    void checkGettingOneFilm() throws Exception {
        createUser(user);

        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].email").value("email@em.ru"))
                .andExpect(jsonPath("$[0].login").value("Логин"))
                .andExpect(jsonPath("$[0].name").value("Имя"))
                .andExpect(jsonPath("$[0].birthday").value("2000-10-20"));
    }

    @Test
    void checkGettingTwoFilms() throws Exception {
        createUser(user);
        createUser(user2);

        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].email").value("email@em.ru"))
                .andExpect(jsonPath("$[0].login").value("Логин"))
                .andExpect(jsonPath("$[0].name").value("Имя"))
                .andExpect(jsonPath("$[0].birthday").value("2000-10-20"))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].email").value("email@em2.ru"))
                .andExpect(jsonPath("$[1].login").value("Логин 2"))
                .andExpect(jsonPath("$[1].name").value("Имя 2"))
                .andExpect(jsonPath("$[1].birthday").value("1990-10-20"));
    }

    @Test
    void checkGettingNoOneFilm() throws Exception {
        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
