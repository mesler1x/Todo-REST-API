package com.roslabsystem.todo.adapter.web;

import com.roslabsystem.todo.adapter.web.dto.request.TodoAndTaskRequest;
import com.roslabsystem.todo.adapter.web.dto.request.UpdateTaskRequest;
import com.roslabsystem.todo.adapter.web.dto.response.TodoResponse;
import com.roslabsystem.todo.service.TaskService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TaskController {
    TaskService taskService;


    @PostMapping
    public TodoResponse createTask(@RequestBody TodoAndTaskRequest todoAndTaskRequest) {
        return taskService.createTask(todoAndTaskRequest);
    }

    @PatchMapping
    public TodoResponse updateTask(@RequestBody UpdateTaskRequest updateTaskRequest) {
        return taskService.updateTask(updateTaskRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        taskService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody TodoAndTaskRequest todoAndTaskRequest) {
        taskService.deleteByRequest(todoAndTaskRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
