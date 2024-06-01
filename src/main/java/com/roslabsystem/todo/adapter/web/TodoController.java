package com.roslabsystem.todo.adapter.web;

import com.roslabsystem.todo.adapter.web.dto.request.TodoRequest;
import com.roslabsystem.todo.adapter.web.dto.response.TodoResponse;
import com.roslabsystem.todo.service.TodoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TodoController {
    TodoService todoService;

    @PostMapping
    public TodoResponse create(@RequestBody TodoRequest todoRequest) {
        return todoService.createTodo(todoRequest);
    }

    @GetMapping("/{id}")
    public TodoResponse get(@PathVariable Long id) {
        return todoService.getTodoById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return new ResponseEntity<>(todoService.deleteById(id), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam String todoName) {
        return new ResponseEntity<>(todoService.deleteByName(todoName), HttpStatus.OK);
    }

    @GetMapping
    public TodoResponse getTodo(@RequestParam String todoName) {
        return todoService.getTodoByName(todoName);
    }
}
