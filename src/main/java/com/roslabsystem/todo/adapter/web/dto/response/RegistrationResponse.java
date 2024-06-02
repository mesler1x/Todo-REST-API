package com.roslabsystem.todo.adapter.web.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record RegistrationResponse(String username, String encodedPassword, List<String> roles) {
}
