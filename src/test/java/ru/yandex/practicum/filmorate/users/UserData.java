package ru.yandex.practicum.filmorate.users;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserData {
    public static final User user = User.builder()
            .name("Имя")
            .login("Логин")
            .email("email@em.ru")
            .birthday(LocalDate.of(2000, 10, 20))
            .build();
}
