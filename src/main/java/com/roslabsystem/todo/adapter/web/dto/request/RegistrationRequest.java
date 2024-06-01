package com.roslabsystem.todo.adapter.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(@NotNull(message = "username should not be null")
                                  @NotBlank(message = "username should not be blank")
                                  String username,
                                  @NotNull(message = "password should not be null")
                                  @NotBlank(message = "password should not be blank")
                                  @Size(min = 4, message = "password length should be more than 4 characters")
                                  String password) {
}
