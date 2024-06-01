package com.roslabsystem.todo.adapter.web.dto.response;

import java.util.List;

public record TodoResponse(Long id, String todoName, List<TaskResponse> taskResponse) {
}
