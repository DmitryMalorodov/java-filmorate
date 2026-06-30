package ru.yandex.practicum.filmorate.model.review;

import lombok.Data;

@Data
public class ReviewReaction {
    private Long id;
    private Long userId;
    private Long reviewId;
    private String reactionType;

}