package ru.yandex.practicum.filmorate.matcher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.yandex.practicum.filmorate.exception.Error;
import ru.yandex.practicum.filmorate.exception.ValidationErrorResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


public class ResponseBodyMatcher {
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


    public <T> ResultMatcher containsObjectAsJson(Object expectedObject, Class<T> targetClass) {
        return mvcResult ->{
            String json = mvcResult.getResponse().getContentAsString();
            T actualObject = objectMapper.readValue(json, targetClass);
            assertThat(actualObject).usingRecursiveComparison().isEqualTo(expectedObject);
        };
    }
    public <T> ResultMatcher containsListAsJson(Object expectedObject, TypeReference<List<T>> targetType) {
        return mvcResult ->{
            String json = mvcResult.getResponse().getContentAsString();
            List<T> actualObject = objectMapper.readValue(json, targetType);
            assertThat(actualObject).usingRecursiveComparison().isEqualTo(expectedObject);
        };
    }
    public ResultMatcher containsError(String expectedFieldName, String expectedMessage){
        return result -> {
            String json = result.getResponse().getContentAsString();
            ValidationErrorResponse errorResult = objectMapper.readValue(json,ValidationErrorResponse.class);
            List<Error> fieldErrors = errorResult.getErrors().stream()
                    .filter(error -> error.getFieldName().equals(expectedFieldName))
                    .filter(error -> error.getMessage().equals(expectedMessage))
                    .collect(Collectors.toList());
            assertThat(fieldErrors)
                    .hasSize(1)
                    .withFailMessage("expecting exactly 1 error message"
                                    + "with field name '%s' and message '%s'",
                            expectedFieldName,
                            expectedMessage);
        };
    }
    public static ResponseBodyMatcher responseBody(){
        return new ResponseBodyMatcher();
    }
}
