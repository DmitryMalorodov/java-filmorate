package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.director.Director;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.model.user.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MainTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected void checkValidationError(ResultActions response, String expMessage) throws Exception {
        response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(expMessage));
    }

    protected Long getIdFromObject(ResultActions response) throws Exception {
        return JsonPath.parse(response
                .andReturn()
                .getResponse()
                .getContentAsString()).read("$.id", Long.class);
    }

    protected ResultActions createUser(User user) throws Exception {
        return mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));
    }

    protected ResultActions createFilm(Film film) throws Exception {
        return mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(film)));
    }

    protected ResultActions createDirector(Director director) throws Exception {
        return mockMvc.perform(post("/directors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(director)));
    }

    protected Long getReviewIdFromObject(ResultActions response) throws Exception {
        return JsonPath.parse(response
                .andReturn()
                .getResponse()
                .getContentAsString()).read("$.reviewId", Long.class);
    }

    protected ResultActions createReviewRequest(Review review) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(review)));
    }

    protected Review createReview(Review review) throws Exception {
        String content = createReviewRequest(review)
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(content, Review.class);
    }

    protected ResultActions changeReview(Review review) throws Exception {
        return mockMvc.perform(put("/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(review)));
    }
}
