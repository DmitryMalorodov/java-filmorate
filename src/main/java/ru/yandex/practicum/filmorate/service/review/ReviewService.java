package ru.yandex.practicum.filmorate.service.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.ReviewReactionRepository;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.review.ReactionType;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewReactionRepository reviewReactionRepository;
    private final UserService userService;
    private final FilmService filmService;

    //GET /reviews/{id}
    public ReviewDto getReviewById(Long reviewId) {
        return ReviewMapper.mapToReviewDto(reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Отзыв не найден с id: " + reviewId)));
    }

    //GET /reviews?filmId={filmId}&count={count}
    public List<ReviewDto> getReviews(Long filmId, int count) {
        return reviewRepository.findByFilmId(filmId, count)
                .stream()
                .map(ReviewMapper::mapToReviewDto)
                .collect(Collectors.toList());
    }

    //POST /reviews
    public  ReviewDto createReview(Review review) {
        userService.getUserById(review.getUserId());
        filmService.getFilmById(review.getFilmId());
        return ReviewMapper.mapToReviewDto(reviewRepository.save(review));
    }

    //PUT /reviews
    public ReviewDto updateReview(Review newReview) {
        Review oldReview = reviewRepository.findById(newReview.getReviewId())
                .orElseThrow(() -> new NotFoundException("Отзыв не найден с id: " + newReview.getReviewId()));
        oldReview.setContent(newReview.getContent());
        oldReview.setIsPositive(newReview.getIsPositive());
        reviewRepository.update(oldReview);
        return ReviewMapper.mapToReviewDto(oldReview);
    }

    //DELETE /reviews/{id}
    public void deleteReview(Long reviewId) {
        getReviewById(reviewId);
        reviewRepository.delete(reviewId);
    }

    //PUT /reviews/{id}/like/{userId}
    public void addLike(Long reviewId, Long userId) {
        getReviewById(reviewId);
        userService.getUserById(userId);
        reviewReactionRepository.getReactionType(reviewId, userId)
                .ifPresentOrElse(
                        reaction -> {
                            if (reaction.equals(ReactionType.LIKE)) {
                                reviewReactionRepository.removeReaction(reviewId, userId);
                                reviewRepository.updateCounters(reviewId, -1, 0);
                            } else if (reaction.equals(ReactionType.DISLIKE)) {
                                reviewReactionRepository.updateReaction(reviewId, userId, ReactionType.LIKE);
                                reviewRepository.updateCounters(reviewId, +1, -1);
                            }
                        },
                        () -> {
                            reviewReactionRepository.addReaction(reviewId, userId, ReactionType.LIKE);
                            reviewRepository.updateCounters(reviewId, +1, 0);
                        }
                );
    }

    //PUT /reviews/{id}/dislike/{userId}
    public void addDislike(Long reviewId, Long userId) {
        getReviewById(reviewId);
        userService.getUserById(userId);

        Optional<String> existingReactionOpt = reviewReactionRepository.getReactionType(reviewId, userId);

        if (existingReactionOpt.isPresent()) {
            String existing = existingReactionOpt.get();

            if (existing.equals(ReactionType.DISLIKE.name())) {
            } else if (existing.equals(ReactionType.LIKE.name())) {
                reviewReactionRepository.updateReaction(reviewId, userId, ReactionType.DISLIKE);
                reviewRepository.updateCounters(reviewId, -1, +1);
            }
        } else {
            // Новая реакция — дизлайк
            reviewReactionRepository.addReaction(reviewId, userId, ReactionType.DISLIKE);
            reviewRepository.updateCounters(reviewId, 0, +1);
        }
    }

    //DELETE /reviews/{id}/like/{userId}
    public void removeLike(Long reviewId, Long userId) {
        getReviewById(reviewId);
        userService.getUserById(userId);

        reviewReactionRepository.getReactionType(reviewId, userId)
                .filter(reaction -> reaction.equals(ReactionType.LIKE.name()))
                .ifPresent(reaction -> {
                    reviewReactionRepository.removeReaction(reviewId, userId);
                    reviewRepository.updateCounters(reviewId, -1, 0);
                });
    }

    //DELETE /reviews/{id}/dislike/{userId}
    public void removeDislike(Long reviewId, Long userId) {
        getReviewById(reviewId);
        userService.getUserById(userId);

        reviewReactionRepository.getReactionType(reviewId, userId)
                .filter(reaction -> reaction.equals(ReactionType.DISLIKE.name()))
                .ifPresent(reaction -> {
                    reviewReactionRepository.removeReaction(reviewId, userId);
                    reviewRepository.updateCounters(reviewId, 0, -1);
                });
    }
}