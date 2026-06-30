package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.marker.OnCreate;
import ru.yandex.practicum.filmorate.marker.OnUpdate;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.Collection;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable final Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public Collection<UserDto> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public UserDto create(@Validated(OnCreate.class) @RequestBody final User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public UserDto update(@Validated(OnUpdate.class) @RequestBody final User newUser) {
        return userService.updateUser(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable final Long id, @PathVariable final Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable final Long id, @PathVariable final Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherUserId}")
    public Collection<UserDto> getCommonFriendsList(@PathVariable final Long id, @PathVariable final Long otherUserId) {
        return userService.getCommonFriendsList(id, otherUserId);
    }

    @GetMapping("/{id}/friends")
    public Collection<UserDto> getFriendsList(@PathVariable final Long id) {
        return userService.getUserFriendsList(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable final Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}/recommendations")
    public Collection<FilmDto> getRecommendedFilms(@PathVariable Long id) {
        return userService.getRecommendations(id);
    }
}
