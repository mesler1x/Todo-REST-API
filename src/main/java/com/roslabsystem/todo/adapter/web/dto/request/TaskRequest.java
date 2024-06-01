package com.roslabsystem.todo.adapter.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskRequest(@NotNull(message = "task name should not be null")
                          @NotBlank(message = "task name should not be blank")
                          String taskName) {
}
