package ru.yandex.practicum.filmorate.constant.endpoint;

public class UserEndpoints {
    public static final String USERS = "/users";
    public static final String USERS_ID = "/users/{id}";
    public static final String USERS_ID_FRIENDS_COMMON_OTHER_ID = "/users/{id}/friends/common/{otherId}";
    public static final String USERS_ID_FRIENDS = "/users/{id}/friends";
    public static final String USERS_ID_FRIENDS_FRIEND_ID = "/users/{id}/friends/{friendId}";
}
