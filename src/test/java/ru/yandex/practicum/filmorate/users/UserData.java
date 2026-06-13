package ru.yandex.practicum.filmorate.users;

import ru.yandex.practicum.filmorate.model.user.User;

import java.time.LocalDate;

public class UserData {
    public static final User user = User.builder()
            .name("Имя")
            .login("Логин")
            .email("email@em.ru")
            .birthday(LocalDate.of(2000, 10, 20))
            .build();

    public static final User user2 = User.builder()
            .name("Имя")
            .login("Логин2")
            .email("email@em2.ru")
            .birthday(LocalDate.of(2000, 10, 20))
            .build();

    public static final User user3 = User.builder()
            .name("Имя")
            .login("Логин3")
            .email("email@em3.ru")
            .birthday(LocalDate.of(2000, 10, 20))
            .build();
}
