package com.roslabsystem.todo.adapter.web.advice;

import com.roslabsystem.todo.adapter.web.dto.response.BasicErrorResponse;
import com.roslabsystem.todo.adapter.web.exceptions.AlreadyExistException;
import com.roslabsystem.todo.adapter.web.exceptions.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class CommonAdvice extends ResponseEntityExceptionHandler {
    private final String VALIDATION_ERROR = "validation error";

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BasicErrorResponse> handleNotFound(NotFoundException ex) {
        return new ResponseEntity<>(new BasicErrorResponse(ex.getCode(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<BasicErrorResponse> handleAlreadyExist(AlreadyExistException ex) {
        return new ResponseEntity<>(new BasicErrorResponse(ex.getCode(), ex.getMessage()), HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<BasicErrorResponse> errors = ex.getBindingResult()
                .getFieldErrors().stream().
                map(fieldError -> new BasicErrorResponse(
                        VALIDATION_ERROR,
                        fieldError.getField(),
                        fieldError.getDefaultMessage())).toList();
        return ResponseEntity.badRequest().body(errors);
    }

    @Override
    protected ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>(new BasicErrorResponse("EXPECTATION_FAILED", "file larger than 5 MB"), HttpStatus.EXPECTATION_FAILED);
    }
}
