package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.constant.endpoint.UserEndpoints;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.marker.OnCreate;
import ru.yandex.practicum.filmorate.marker.OnUpdate;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(UserEndpoints.USERS_ID)
    public UserDto getUserById(@PathVariable final Long id) {
        return userService.getUserById(id);
    }

    @GetMapping(UserEndpoints.USERS)
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @PostMapping(UserEndpoints.USERS)
    public UserDto create(@Validated(OnCreate.class) @RequestBody final User user) {
        return userService.createUser(user);
    }

    @PutMapping(UserEndpoints.USERS)
    public UserDto update(@Validated(OnUpdate.class) @RequestBody final User newUser) {
        return userService.updateUser(newUser);
    }

    @PutMapping(UserEndpoints.USERS_ID_FRIENDS_FRIEND_ID)
    public void addFriend(@PathVariable final Long id, @PathVariable final Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(UserEndpoints.USERS_ID_FRIENDS_FRIEND_ID)
    public void deleteFriend(@PathVariable final Long id, @PathVariable final Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping(UserEndpoints.USERS_ID_FRIENDS_COMMON_OTHER_ID)
    public List<UserDto> getCommonFriendsList(@PathVariable final Long id, @PathVariable final Long otherUserId) {
        return userService.getCommonFriendsList(id, otherUserId);
    }

    @GetMapping(UserEndpoints.USERS_ID_FRIENDS)
    public List<UserDto> getFriendsList(@PathVariable final Long id) {
        return userService.getUserFriendsList(id);
    }









    @GetMapping("/userss/{uid}")
    public User findUserById(@PathVariable final Long id) {
        return userService.findUserById(id);
    }
}
