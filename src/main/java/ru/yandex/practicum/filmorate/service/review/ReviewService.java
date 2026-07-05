package ru.yandex.practicum.filmorate.service.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dal.ReviewReactionRepository;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.model.event.OperationType;
import ru.yandex.practicum.filmorate.model.review.ReactionType;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.service.event.EventService;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewReactionRepository reviewReactionRepository;
    private final UserService userService;
    private final FilmService filmService;
    private final EventService eventService;

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
    public ReviewDto createReview(Review review) {
        userService.getUserById(review.getUserId());
        filmService.getFilmById(review.getFilmId());
        ReviewDto reviewDto = ReviewMapper.mapToReviewDto(reviewRepository.save(review));
        eventService.addEvent(reviewDto.getUserId(), reviewDto.getReviewId(), EventType.REVIEW, OperationType.ADD);
        return reviewDto;
    }

    //PUT /reviews
    public ReviewDto updateReview(Review newReview) {
        Review oldReview = reviewRepository.findById(newReview.getReviewId())
                .orElseThrow(() -> new NotFoundException("Отзыв не найден с id: " + newReview.getReviewId()));
        if (newReview.getContent() != null) oldReview.setContent(newReview.getContent());
        if (newReview.getIsPositive() != null) oldReview.setIsPositive(newReview.getIsPositive());
        reviewRepository.update(oldReview);
        eventService.addEvent(oldReview.getUserId(), oldReview.getReviewId(), EventType.REVIEW, OperationType.UPDATE);
        return getReviewById(oldReview.getReviewId());
    }

    //DELETE /reviews/{id}
    @Transactional
    public void deleteReview(Long reviewId) {
        ReviewDto review = getReviewById(reviewId);
        eventService.addEvent(review.getUserId(), review.getReviewId(), EventType.REVIEW, OperationType.REMOVE);
        reviewRepository.delete(reviewId);
    }

    // PUT /reviews/{id}/like/{userId}
    public void addLike(Long reviewId, Long userId) {
        addReaction(reviewId, userId, ReactionType.LIKE);
    }

    // PUT /reviews/{id}/dislike/{userId}
    public void addDislike(Long reviewId, Long userId) {
        addReaction(reviewId, userId, ReactionType.DISLIKE);
    }

    // DELETE /reviews/{id}/like/{userId}
    public void removeLike(Long reviewId, Long userId) {
        removeReaction(reviewId, userId, ReactionType.LIKE);
    }

    // DELETE /reviews/{id}/dislike/{userId}
    public void removeDislike(Long reviewId, Long userId) {
        removeReaction(reviewId, userId, ReactionType.DISLIKE);
    }


    private void addReaction(Long reviewId, Long userId, ReactionType type) {
        getReviewById(reviewId);
        userService.getUserById(userId);

        ReactionType opposite = type == ReactionType.LIKE ? ReactionType.DISLIKE : ReactionType.LIKE;
        int typeDelta = type == ReactionType.LIKE ? +1 : -1;

        reviewReactionRepository.getReactionType(reviewId, userId)
                .ifPresentOrElse(
                        reaction -> {
                            if (reaction.equals(opposite.name())) {
                                reviewReactionRepository.updateReaction(reviewId, userId, type);
                                reviewRepository.updateCounters(reviewId, typeDelta, -typeDelta);
                            }
                        },
                        () -> {
                            reviewReactionRepository.addReaction(reviewId, userId, type);
                            reviewRepository.updateCounters(reviewId, typeDelta, 0);
                        }
                );
    }

    private void removeReaction(Long reviewId, Long userId, ReactionType type) {
        getReviewById(reviewId);
        userService.getUserById(userId);

        int typeDelta = type == ReactionType.LIKE ? -1 : +1;

        reviewReactionRepository.getReactionType(reviewId, userId)
                .filter(reaction -> reaction.equals(type.name()))
                .ifPresent(reaction -> {
                    reviewReactionRepository.removeReaction(reviewId, userId);
                    reviewRepository.updateCounters(reviewId, typeDelta, 0);
                });
    }
}