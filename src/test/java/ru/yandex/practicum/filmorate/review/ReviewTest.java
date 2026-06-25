package ru.yandex.practicum.filmorate.review;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.MainTest;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;
import ru.yandex.practicum.filmorate.model.review.Review;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static ru.yandex.practicum.filmorate.GeneralAssertions.isEqual;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReviewTest extends MainTest {
    protected final ReviewRepository reviewRepository;

    protected static final String REVIEWS = "/reviews";
    protected static final String REVIEWS_ID = "/reviews/{id}";
    protected static final String REVIEWS_ID_LIKE = "/reviews/{id}/like/{userId}";
    protected static final String REVIEWS_ID_DISLIKE = "/reviews/{id}/dislike/{userId}";

    protected Review createReview(Review review) throws Exception {
        String content = createReviewRequest(review)
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(content, Review.class);
    }

    public ResultActions createReviewRequest(Review review) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(REVIEWS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(review)));
    }

    public ResultActions changeReview(Review review) throws Exception {
        return mockMvc.perform(put(REVIEWS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(review)));
    }

    protected void checkReview(Review actual, Review expected, SoftAssertions softAssert) {
        softAssert.assertThat(actual.getReviewId())
                .as("reviewId")
                .isEqualTo(expected.getReviewId());

        softAssert.assertThat(actual.getContent())
                .as("content")
                .isEqualTo(expected.getContent());

        softAssert.assertThat(actual.getIsPositive())
                .as("isPositive")
                .isEqualTo(expected.getIsPositive());

        softAssert.assertThat(actual.getUserId())
                .as("userId")
                .isEqualTo(expected.getUserId());

        softAssert.assertThat(actual.getFilmId())
                .as("filmId")
                .isEqualTo(expected.getFilmId());

        if (expected.getUseful() != null) {
            softAssert.assertThat(actual.getUseful())
                    .as("useful")
                    .isEqualTo(expected.getUseful());
        }

        if (expected.getLikesCount() != null) {
            softAssert.assertThat(actual.getLikesCount())
                    .as("likesCount")
                    .isEqualTo(expected.getLikesCount());
        }

        if (expected.getDislikesCount() != null) {
            softAssert.assertThat(actual.getDislikesCount())
                    .as("dislikesCount")
                    .isEqualTo(expected.getDislikesCount());
        }
    }
}
