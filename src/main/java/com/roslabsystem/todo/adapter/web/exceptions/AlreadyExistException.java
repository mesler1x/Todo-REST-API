package com.roslabsystem.todo.adapter.web.exceptions;

import lombok.Getter;

@Getter
public class AlreadyExistException extends RuntimeException {
    private final String code = "CONFLICT";

    public AlreadyExistException(String type) {
        super("This " + type + " Already Exist");
    }
}
