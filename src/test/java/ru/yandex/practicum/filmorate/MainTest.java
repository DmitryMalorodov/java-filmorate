package ru.yandex.practicum.filmorate;

import com.jayway.jsonpath.JsonPath;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MainTest {

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
}
