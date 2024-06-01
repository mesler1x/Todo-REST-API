package com.roslabsystem.todo.adapter.web.dto.response;

public record FileResponse(String name, String url, String type, long size, TaskResponse taskResponse) {
}
