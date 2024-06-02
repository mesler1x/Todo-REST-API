package com.roslabsystem.todo.adapter.web;

import com.roslabsystem.todo.adapter.web.dto.request.TodoAndTaskRequest;
import com.roslabsystem.todo.adapter.web.dto.request.UpdateTaskRequest;
import com.roslabsystem.todo.adapter.web.dto.response.TaskResponse;
import com.roslabsystem.todo.domain.user.UserEntity;
import com.roslabsystem.todo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Контроллер задачи")
public class TaskController {
    TaskService taskService;

    @Operation(summary = "Создание задачи и прикрепление её к конкретному TODO листу")
    @PostMapping
    public TaskResponse createTask(@AuthenticationPrincipal UserEntity user, @Valid @RequestBody TodoAndTaskRequest todoAndTaskRequest) {
        return taskService.createTask(user, todoAndTaskRequest);
    }

    @Operation(summary = "Обновление задачи",
            description = "Можно как обновить описание, так и поставить задачу выполненной или наоборот снять выполнение")
    @PatchMapping
    public TaskResponse updateTask(@AuthenticationPrincipal UserEntity user, @Valid @RequestBody UpdateTaskRequest updateTaskRequest) {
        return taskService.updateTask(user, updateTaskRequest);
    }

    @Operation(summary = "Удаление задачи по её id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserEntity user, @PathVariable Long id) {
        taskService.deleteById(user, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удаление задачи по её описанию и имени TODO листа, к которой она прикреплена")
    @DeleteMapping
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserEntity user, @Valid @RequestBody TodoAndTaskRequest todoAndTaskRequest) {
        taskService.deleteByRequest(user, todoAndTaskRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
