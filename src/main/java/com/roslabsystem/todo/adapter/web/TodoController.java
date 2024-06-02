package com.roslabsystem.todo.adapter.web;

import com.roslabsystem.todo.adapter.web.dto.request.TodoRequest;
import com.roslabsystem.todo.adapter.web.dto.response.TodoResponse;
import com.roslabsystem.todo.domain.user.UserEntity;
import com.roslabsystem.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Validated
@Tag(name = "Контроллер TODO листа")
public class TodoController {
    TodoService todoService;

    @Operation(summary = "Создание TODO листа и прикрепление его за определенным пользователем")
    @PostMapping
    public TodoResponse create(@AuthenticationPrincipal UserEntity user,
                               @Valid @RequestBody TodoRequest todoRequest) {
        return todoService.createTodo(user, todoRequest);
    }

    @Operation(summary = "Получение TODO листа по его Id")
    @GetMapping("/{id}")
    public TodoResponse get(@AuthenticationPrincipal UserEntity user,
                            @PathVariable Long id) {
        return todoService.getTodoById(user, id);
    }

    @Operation(summary = "Удаление TODO листа по его Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserEntity user,
                                    @PathVariable Long id) {
        return new ResponseEntity<>(todoService.deleteById(user, id), HttpStatus.OK);
    }
    @Operation(summary = "Удаление TODO листа по его имени")
    @DeleteMapping
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserEntity user,
                                    @Valid @NotBlank(message = "todo name should not be null") @RequestParam String todoName) {
        return new ResponseEntity<>(todoService.deleteByName(user, todoName), HttpStatus.OK);
    }

    @Operation(summary = "Получение TODO листа по его имени")
    @GetMapping
    public TodoResponse getTodo(@AuthenticationPrincipal UserEntity user,
                                @Valid @NotBlank(message = "todo name should not be null") @RequestParam String todoName) {
        return todoService.getTodoByName(user, todoName);
    }
}
