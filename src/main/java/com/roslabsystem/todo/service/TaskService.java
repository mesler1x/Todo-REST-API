package com.roslabsystem.todo.service;

import com.roslabsystem.todo.adapter.repository.TaskRepository;
import com.roslabsystem.todo.adapter.repository.TodoRepository;
import com.roslabsystem.todo.adapter.web.dto.request.TodoAndTaskRequest;
import com.roslabsystem.todo.adapter.web.dto.request.UpdateTaskRequest;
import com.roslabsystem.todo.adapter.web.dto.response.TaskResponse;
import com.roslabsystem.todo.adapter.web.exceptions.NotFoundException;
import com.roslabsystem.todo.domain.TaskEntity;
import com.roslabsystem.todo.domain.TodoEntity;
import com.roslabsystem.todo.domain.user.UserEntity;
import com.roslabsystem.todo.service.mapper.TaskMapper;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TaskService {
    TaskRepository taskRepository;
    TodoRepository todoRepository;
    TaskMapper taskMapper;

    public TaskResponse createTask(UserEntity user, TodoAndTaskRequest todoAndTaskRequest) {
        TodoEntity todo = todoRepository.findByTodoNameAndUser(todoAndTaskRequest.todoName(), user).orElseThrow(() ->
                new NotFoundException(String.format("todo with name %s and user with id %s", todoAndTaskRequest.todoName(), user.getId())));

        TaskEntity taskEntity = taskMapper.requestToEntity(todoAndTaskRequest);
        taskEntity.setTodo(todo);
        TaskEntity saved = taskRepository.save(taskEntity);

        return taskMapper.entityToResponse(saved);
    }


    public TaskResponse updateTask(UserEntity user, UpdateTaskRequest updateTaskRequest) {
        TaskEntity task = taskRepository.findByIdAndUser(updateTaskRequest.id(), user).orElseThrow(() ->
                new NotFoundException(String.format("task with id: %s and user with id %s", updateTaskRequest.id(), user.getId())));

        taskRepository.updateTaskById(updateTaskRequest.id(), updateTaskRequest.isCompleted(), updateTaskRequest.description());
        task.setIsCompleted(updateTaskRequest.isCompleted());
        task.setDescription(updateTaskRequest.description());

        return taskMapper.entityToResponse(task);
    }

    @Transactional
    public void deleteById(UserEntity user, Long id) {
        taskRepository.findByIdAndUser(id, user).orElseThrow(() ->
                new NotFoundException(String.format("task with id: %s and user id: %s", id, user.getId())));

        taskRepository.deleteByIdAndUser(id, user);
    }

    @Transactional
    public void deleteByRequest(UserEntity user, TodoAndTaskRequest todoAndTaskRequest) {
        todoRepository.findByTodoNameAndUser(todoAndTaskRequest.todoName(), user).orElseThrow(()
                -> new NotFoundException(String.format("task with description: '%s' and user with id: %s", todoAndTaskRequest.taskDescription(), user.getId())));

        TaskEntity taskEntity = taskRepository.findByDescriptionAndUser(todoAndTaskRequest.taskDescription(), user).orElseThrow(() ->
                new NotFoundException(String.format("task with description %s", todoAndTaskRequest.taskDescription())));

        taskRepository.deleteByIdAndUser(taskEntity.getId(), user);
    }
}
