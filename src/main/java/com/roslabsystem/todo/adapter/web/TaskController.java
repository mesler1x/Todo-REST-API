package com.roslabsystem.todo.adapter.web;

import com.roslabsystem.todo.adapter.web.dto.request.TodoAndTaskRequest;
import com.roslabsystem.todo.adapter.web.dto.request.UpdateTaskRequest;
import com.roslabsystem.todo.adapter.web.dto.response.TaskResponse;
import com.roslabsystem.todo.adapter.web.dto.response.TodoResponse;
import com.roslabsystem.todo.domain.user.UserEntity;
import com.roslabsystem.todo.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Validated
public class TaskController {
    TaskService taskService;

    @PostMapping
    public TaskResponse createTask(@AuthenticationPrincipal UserEntity user, @Valid @RequestBody TodoAndTaskRequest todoAndTaskRequest) {
        return taskService.createTask(user, todoAndTaskRequest);
    }

    @PatchMapping
    public TaskResponse updateTask(@AuthenticationPrincipal UserEntity user, @Valid @RequestBody UpdateTaskRequest updateTaskRequest) {
        return taskService.updateTask(user, updateTaskRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserEntity user, @PathVariable Long id) {
        taskService.deleteById(user, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserEntity user, @Valid @RequestBody TodoAndTaskRequest todoAndTaskRequest) {
        taskService.deleteByRequest(user, todoAndTaskRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
