package ru.yandex.practicum.filmorate.model.review;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import static ru.yandex.practicum.filmorate.constant.message.ReviewValidationMessages.*;

@Data
@Builder(toBuilder = true)
public class Review {
    private Long reviewId;

    @NotBlank(message = CONTENT_BLANK_MESSAGE)
    @Size(max = 255, message = CONTENT_MAX_LENGTH_MESSAGE)
    private String content;

    @NotNull(message = REVIEW_TYPE_NULL_MESSAGE)
    private Boolean isPositive;

    @NotNull(message = USER_ID_INVALID_MESSAGE)
    private Long userId;

    @NotNull(message = FILM_ID_INVALID_MESSAGE)
    private Long filmId;

    private Integer useful;
    private Integer likesCount;
    private Integer dislikesCount;
}
