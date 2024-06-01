package com.roslabsystem.todo.adapter.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TodoRequest( @NotNull(message = "todo name name should not be null")
                           @NotBlank(message = "todo name name should not be blank") String todoName) {
}
