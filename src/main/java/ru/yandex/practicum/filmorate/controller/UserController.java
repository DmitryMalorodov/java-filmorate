package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.marker.OnCreate;
import ru.yandex.practicum.filmorate.marker.OnUpdate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserStorage userStorage;

    @Autowired
    public UserController(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @PostMapping
    public User create(@Validated(OnCreate.class) @RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping
    public User update(@Validated(OnUpdate.class) @RequestBody User newUser) {
        return userStorage.update(newUser);
    }
}
