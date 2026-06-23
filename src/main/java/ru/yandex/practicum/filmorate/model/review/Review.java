package ru.yandex.practicum.filmorate.model.review;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Review {
    private Long reviewId;

    @NotBlank(message = "Содержание отзыва не может быть пустым")
    private String content;

    @NotNull(message = "Тип отзыва должен быть указан")
    private Boolean isPositive;

    @NotNull(message = "ID пользователя должен быть указан")
    private Long userId;

    @NotNull(message = "ID фильма должен быть указан")
    private Long filmId;

    private Integer useful;
    private Integer likesCount;
    private Integer dislikesCount;
}
