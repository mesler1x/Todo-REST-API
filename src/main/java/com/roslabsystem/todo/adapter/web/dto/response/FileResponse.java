package com.roslabsystem.todo.adapter.web.dto.response;

public record FileResponse(Long fileId, String name, String url, String type, long size, Long taskId) {
}
