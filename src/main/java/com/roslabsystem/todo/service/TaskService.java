package com.roslabsystem.todo.service;

import com.roslabsystem.todo.adapter.repository.TaskRepository;
import com.roslabsystem.todo.adapter.repository.TodoRepository;
import com.roslabsystem.todo.adapter.web.dto.request.TodoAndTaskRequest;
import com.roslabsystem.todo.adapter.web.dto.request.UpdateTaskRequest;
import com.roslabsystem.todo.adapter.web.dto.response.TodoResponse;
import com.roslabsystem.todo.adapter.web.exceptions.NotFoundException;
import com.roslabsystem.todo.domain.TaskEntity;
import com.roslabsystem.todo.domain.TodoEntity;
import com.roslabsystem.todo.service.mapper.TaskMapper;
import com.roslabsystem.todo.service.mapper.TodoMapper;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TaskService {
    TaskRepository taskRepository;
    TodoRepository todoRepository;
    TaskMapper taskMapper;
    TodoMapper todoMapper;

    public TodoResponse createTask(TodoAndTaskRequest todoAndTaskRequest) {
        TodoEntity todo = todoRepository.findByTodoName(todoAndTaskRequest.todoName()).orElseThrow(() ->
                new NotFoundException(String.format("todo with name %s", todoAndTaskRequest.todoName())));

        TaskEntity taskEntity = taskMapper.requestToEntity(todoAndTaskRequest);
        taskEntity.setTodo(todo);
        TaskEntity saved = taskRepository.save(taskEntity);
        //todo.addTask(saved);

        return todoMapper.entityToResponse(todo);
    }


    public TodoResponse updateTask(UpdateTaskRequest updateTaskRequest) {
        TaskEntity task = taskRepository.findById(updateTaskRequest.id()).orElseThrow(() ->
                new NotFoundException("task with this id"));

        taskRepository.updateTaskByIdSetIsCompletedToTrue(updateTaskRequest.id(), updateTaskRequest.isCompleted());
        //task.setIsCompleted(updateTaskRequest.isCompleted());

        return todoMapper.entityToResponse(task.getTodo());
    }

    @Transactional
    public void deleteById(Long id) {
        taskRepository.findById(id).orElseThrow(() ->
                new NotFoundException("task with this id"));

        taskRepository.deleteById(id);
    }

    @Transactional
    public void deleteByRequest(TodoAndTaskRequest todoAndTaskRequest) {
        todoRepository.findByTodoName(todoAndTaskRequest.todoName()).orElseThrow(()
                -> new NotFoundException("todo"));

        TaskEntity taskEntity = taskRepository.findByDescription(todoAndTaskRequest.taskDescription()).orElseThrow(() ->
                new NotFoundException(String.format("task with description %s", todoAndTaskRequest.taskDescription())));

        taskRepository.deleteById(taskEntity.getId());
    }
}
