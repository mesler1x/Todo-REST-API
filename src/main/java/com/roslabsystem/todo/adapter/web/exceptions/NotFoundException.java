package com.roslabsystem.todo.adapter.web.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final String code = "NOT_FOUND";

    public NotFoundException(String type) {
        super("Not found " + type);
    }
}
