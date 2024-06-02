package com.roslabsystem.todo.service;

import com.roslabsystem.todo.adapter.repository.TaskRepository;
import com.roslabsystem.todo.adapter.repository.TodoRepository;
import com.roslabsystem.todo.adapter.web.dto.request.TodoRequest;
import com.roslabsystem.todo.adapter.web.dto.response.TodoResponse;
import com.roslabsystem.todo.adapter.web.exceptions.AlreadyExistException;
import com.roslabsystem.todo.adapter.web.exceptions.NotFoundException;
import com.roslabsystem.todo.domain.TodoEntity;
import com.roslabsystem.todo.domain.user.UserEntity;
import com.roslabsystem.todo.service.mapper.TodoMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TodoService {
    TodoRepository todoRepository;
    TaskRepository taskRepository;
    TodoMapper todoMapper;

    public TodoResponse createTodo(UserEntity user, TodoRequest todoRequest) {
        todoRepository.findByTodoNameAndUser(todoRequest.todoName(), user)
                .ifPresent(
                        existingTodo -> {
                            throw new AlreadyExistException(String.format("todo with name %s", todoRequest.todoName()));
                        }
                );

        TodoEntity todoEntity = todoMapper.requestToEntity(todoRequest);
        todoEntity.setUser(user);
        todoRepository.save(todoEntity);

        return todoMapper.entityToResponse(todoEntity);
    }

    public ResponseEntity<?> deleteById(UserEntity user, Long id) {
        TodoEntity todo = todoRepository.findByIdAndUser(id, user).orElseThrow(() ->
                new NotFoundException(String.format("todo with id: %s and user id: %s", id, user.getId())));

        taskRepository.deleteAllInBatch(todo.getTasks());
        todoRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteByName(UserEntity user, String todoName) {
        TodoEntity todo = todoRepository.findByTodoNameAndUser(todoName, user).orElseThrow(() ->
                new NotFoundException(String.format("todo with todo name: %s and user id: %s", todoName, user.getId())));

        taskRepository.deleteAllInBatch(todo.getTasks());
        todoRepository.deleteById(todo.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public TodoResponse getTodoById(UserEntity user, Long id) {
        TodoEntity todo = todoRepository.findByIdAndUser(id, user).orElseThrow(() ->
                new NotFoundException(String.format("todo with id: %s and user id: %s", id, user.getId())));

        return todoMapper.entityToResponse(todo);
    }

    public TodoResponse getTodoByName(UserEntity user, String todoName) {
        TodoEntity todo = todoRepository.findByTodoNameAndUser(todoName, user).orElseThrow(() ->
                new NotFoundException(String.format("todo with todo name: %s and user id: %s", todoName, user.getId())));

        return todoMapper.entityToResponse(todo);
    }
}
