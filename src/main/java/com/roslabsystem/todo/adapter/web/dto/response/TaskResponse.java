package com.roslabsystem.todo.adapter.web.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record TaskResponse(Long id, String taskDescription, Boolean isCompleted) {
}
