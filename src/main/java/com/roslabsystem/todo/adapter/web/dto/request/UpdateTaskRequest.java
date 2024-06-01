package com.roslabsystem.todo.adapter.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskRequest(@NotNull(message = "id should not be null") Long id,
                                @NotNull(message = "description should not be null")
                                @NotBlank(message = "description should not be blank") String description,
                                @NotNull(message = "isCompleted parameter should not be null")
                                Boolean isCompleted) {
}
