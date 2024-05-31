package com.roslabsystem.todo.adapter.web;

import com.roslabsystem.todo.adapter.web.dto.TodoAndTaskRequest;
import com.roslabsystem.todo.adapter.web.dto.TodoResponse;
import com.roslabsystem.todo.service.TaskService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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

    @PatchMapping("/complete/{id}")
    public TodoResponse completeTaskById(@PathVariable Long id) {
        return taskService.completeTaskById(id);
    }

    @PatchMapping("/unComplete/{id}")
    public TodoResponse unCompleteTaskById(@PathVariable Long id) {
        return taskService.unCompleteById(id);
    }

    @DeleteMapping("/{id}")
    public TodoResponse delete(@PathVariable Long id) {
        return taskService.deleteById(id);
    }

    @DeleteMapping
    public TodoResponse delete(@RequestBody TodoAndTaskRequest todoAndTaskRequest) {
        return taskService.deleteByRequest(todoAndTaskRequest);
    }
}
