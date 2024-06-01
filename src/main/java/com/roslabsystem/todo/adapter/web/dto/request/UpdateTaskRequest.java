package com.roslabsystem.todo.adapter.web.dto.request;

public record UpdateTaskRequest(Long id, String description, Boolean isCompleted) {
}
