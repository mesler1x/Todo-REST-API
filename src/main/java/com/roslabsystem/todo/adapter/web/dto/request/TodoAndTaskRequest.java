package com.roslabsystem.todo.adapter.web.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record TodoAndTaskRequest(
        @NotNull(message = "task name should not be null")
        @NotBlank(message = "task name should not be blank")
        String todoName,
        @NotNull(message = "task description should not be null")
        @NotBlank(message = "task description should not be blank")
        String taskDescription) {
}
