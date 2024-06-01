package com.roslabsystem.todo.adapter.web.dto.response;

public record TaskResponse(Long id, String taskDescription, Boolean isCompleted) {
}
