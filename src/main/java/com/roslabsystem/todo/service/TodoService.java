package com.roslabsystem.todo.service;

import com.roslabsystem.todo.adapter.repository.TaskRepository;
import com.roslabsystem.todo.adapter.repository.TodoRepository;
import com.roslabsystem.todo.adapter.web.dto.TodoRequest;
import com.roslabsystem.todo.adapter.web.dto.TodoResponse;
import com.roslabsystem.todo.adapter.web.exceptions.AlreadyExistException;
import com.roslabsystem.todo.adapter.web.exceptions.NotFoundException;
import com.roslabsystem.todo.domain.TodoEntity;
import com.roslabsystem.todo.service.mapper.TodoMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TodoService {
    TodoRepository todoRepository;
    TaskRepository taskRepository;
    TodoMapper todoMapper;

    public TodoResponse createTodo(TodoRequest todoRequest) {
        Optional<TodoEntity> todo = todoRepository.findByTodoName(todoRequest.todoName());
        if (todo.isPresent())  {
            throw new AlreadyExistException("todo");
        }
        TodoEntity todoEntity = todoRepository.save(todoMapper.requestToEntity(todoRequest));
        return todoMapper.entityToResponse(todoEntity);
    }

    public ResponseEntity<?> deleteById(Long id) {
        Optional<TodoEntity> todo = todoRepository.findById(id);
        if (todo.isEmpty()) {
            throw new NotFoundException("todo with this id");
        }

        taskRepository.deleteAllInBatch(todo.get().getTasks());
        todoRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteByName(String todoName) {
        Optional<TodoEntity> todo = todoRepository.findByTodoName(todoName);
        if (todo.isEmpty()) {
            throw new NotFoundException("todo with this name");
        }

        taskRepository.deleteAllInBatch(todo.get().getTasks());
        todoRepository.deleteById(todo.get().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public TodoResponse getTodoByTodoById(Long id) {
        Optional<TodoEntity> todo = todoRepository.findById(id);
        if (todo.isEmpty()) {
            throw new NotFoundException("todo with this id");
        }

        return todoMapper.entityToResponse(todo.get());
    }

    public TodoResponse getTodoByName(String todoName) {
        Optional<TodoEntity> todo = todoRepository.findByTodoName(todoName);
        if (todo.isEmpty()) {
            throw new NotFoundException("todo with this name");
        }

        return todoMapper.entityToResponse(todo.get());
    }
}
