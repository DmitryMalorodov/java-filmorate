package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.model.review.Review;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewMapper {

    public static ReviewDto mapToReviewDto(Review review) {
        return ReviewDto.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .isPositive(review.getIsPositive())
                .userId(review.getUserId())
                .filmId(review.getFilmId())
                .useful(review.getUseful())
                .likesCount(review.getLikesCount())
                .dislikesCount(review.getDislikesCount())
                .build();
    }

    public static Review mapToReview(ReviewDto reviewDto) {
        return Review.builder()
                .id(reviewDto.getReviewId())
                .content(reviewDto.getContent())
                .isPositive(reviewDto.getIsPositive())
                .userId(reviewDto.getUserId())
                .filmId(reviewDto.getFilmId())
                .useful(reviewDto.getUseful())
                .likesCount(reviewDto.getLikesCount())
                .dislikesCount(reviewDto.getDislikesCount())
                .build();
    }
}