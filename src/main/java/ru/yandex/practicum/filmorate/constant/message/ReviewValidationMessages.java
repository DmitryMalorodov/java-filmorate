package ru.yandex.practicum.filmorate.constant.message;

public class ReviewValidationMessages extends ValidationMessages {

    public static final String CONTENT_BLANK_MESSAGE = "Содержание отзыва не может быть пустым";
    public static final String CONTENT_MAX_LENGTH_MESSAGE = "Содержание отзыва не может быть длиннее 255 символов";
    public static final String USER_ID_INVALID_MESSAGE = "ID пользователя должен быть указан";
    public static final String FILM_ID_INVALID_MESSAGE = "ID фильма должен быть указан";
    public static final String REVIEW_NOT_FOUND_MESSAGE = "Отзыв не найден с id: %d";
    public static final String REVIEW_TYPE_NULL_MESSAGE = "Тип отзыва должен быть указан";
}