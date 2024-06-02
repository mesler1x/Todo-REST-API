package com.roslabsystem.todo.adapter.web.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record TodoRequest(@NotNull(message = "todo name name should not be null")
                          @NotBlank(message = "todo name name should not be blank") String todoName) {
}
