package com.roslabsystem.todo.adapter.web.dto.response;

import java.util.List;

public record RegistrationResponse(String username, String encodedPassword, List<String> roles) {
}