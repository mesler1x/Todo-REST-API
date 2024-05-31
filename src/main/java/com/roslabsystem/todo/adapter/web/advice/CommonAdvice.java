package com.roslabsystem.todo.adapter.web.advice;

import com.roslabsystem.todo.adapter.web.dto.BasicErrorResponse;
import com.roslabsystem.todo.adapter.web.exceptions.AlreadyExistException;
import com.roslabsystem.todo.adapter.web.exceptions.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommonAdvice {

    @ExceptionHandler
    public ResponseEntity<BasicErrorResponse> handleNotFound(NotFoundException ex) {
        return new ResponseEntity<>(new BasicErrorResponse(ex.getCode(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<BasicErrorResponse> handleAlreadyExist(AlreadyExistException ex) {
        return new ResponseEntity<>(new BasicErrorResponse(ex.getCode(), ex.getMessage()), HttpStatus.CONFLICT);
    }
}
