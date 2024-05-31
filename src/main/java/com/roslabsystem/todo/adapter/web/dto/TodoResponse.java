package com.roslabsystem.todo.adapter.web.dto;

import java.util.List;

public record TodoResponse(Long id, String todoName, List<TaskResponse> taskResponse) {
}
