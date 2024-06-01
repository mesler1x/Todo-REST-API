package com.roslabsystem.todo.service;

import com.roslabsystem.todo.adapter.repository.TaskRepository;
import com.roslabsystem.todo.adapter.repository.TodoRepository;
import com.roslabsystem.todo.adapter.web.dto.request.TodoRequest;
import com.roslabsystem.todo.adapter.web.dto.response.TodoResponse;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TodoService {
    TodoRepository todoRepository;
    TaskRepository taskRepository;
    TodoMapper todoMapper;

    public TodoResponse createTodo(TodoRequest todoRequest) {
        todoRepository.findByTodoName(todoRequest.todoName()).orElseThrow(() -> new AlreadyExistException("todo"));

        TodoEntity todoEntity = todoRepository.save(todoMapper.requestToEntity(todoRequest));
        return todoMapper.entityToResponse(todoEntity);
    }

    public ResponseEntity<?> deleteById(Long id) {
        TodoEntity todo = todoRepository.findById(id).orElseThrow(() -> new AlreadyExistException("todo with id: " + id));

        taskRepository.deleteAllInBatch(todo.getTasks());
        todoRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteByName(String todoName) {
        TodoEntity todo = todoRepository.findByTodoName(todoName).orElseThrow(() -> new NotFoundException("todo with name: " + todoName));

        taskRepository.deleteAllInBatch(todo.getTasks());
        todoRepository.deleteById(todo.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public TodoResponse getTodoById(Long id) {
        TodoEntity todo = todoRepository.findById(id).orElseThrow(() -> new NotFoundException("todo with id: " + id));

        return todoMapper.entityToResponse(todo);
    }

    public TodoResponse getTodoByName(String todoName) {
        TodoEntity todo = todoRepository.findByTodoName(todoName).orElseThrow(() -> new NotFoundException("todo with name: " + todoName));

        return todoMapper.entityToResponse(todo);
    }
}
