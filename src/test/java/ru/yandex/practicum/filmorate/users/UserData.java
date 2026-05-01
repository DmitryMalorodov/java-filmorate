package ru.yandex.practicum.filmorate.users;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserData {
    static final User user = User.builder()
            .name("Имя")
            .login("Логин")
            .email("email@em.ru")
            .birthday(LocalDate.of(2000, 10, 20))
            .build();

    static final User user2 = User.builder()
            .name("Имя 2")
            .login("Логин 2")
            .email("email@em2.ru")
            .birthday(LocalDate.of(1990, 10, 20))
            .build();

    static final User userEmailNull = User.builder()
            .name("Имя 2")
            .login("Логин 2")
            .email(null)
            .birthday(LocalDate.of(1990, 10, 20))
            .build();

    static final User userEmailNotCorrect = User.builder()
            .name("Имя 2")
            .login("Логин 2")
            .email("email")
            .birthday(LocalDate.of(1990, 10, 20))
            .build();

    static final User userLoginNull = User.builder()
            .name("Имя")
            .login(null)
            .email("email@em.ru")
            .birthday(LocalDate.of(2000, 10, 20))
            .build();

    static final User userLoginBlank = User.builder()
            .name("Имя")
            .login(" ")
            .email("email@em.ru")
            .birthday(LocalDate.of(2000, 10, 20))
            .build();

    static final User userNameNull = User.builder()
            .name(null)
            .login("login")
            .email("email@em.ru")
            .birthday(LocalDate.of(2000, 10, 20))
            .build();

    static final User userNameBlank = User.builder()
            .name(" ")
            .login("login")
            .email("email@em.ru")
            .birthday(LocalDate.of(2000, 10, 20))
            .build();

    static final User userBirthdayNow = User.builder()
            .name("Имя")
            .login("login")
            .email("email@em.ru")
            .birthday(LocalDate.now())
            .build();

    static final User userBirthdayFuture = User.builder()
            .name("Имя")
            .login("login")
            .email("email@em.ru")
            .birthday(LocalDate.now().plusDays(1))
            .build();
}
