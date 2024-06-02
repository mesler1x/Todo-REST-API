package com.roslabsystem.todo.adapter.web.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BasicErrorResponse(String code, String field, String message) {
    public BasicErrorResponse(String code, String message) {
        this(code, null, message);
    }
}
