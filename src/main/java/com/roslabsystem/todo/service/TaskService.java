package com.roslabsystem.todo.service;

import com.roslabsystem.todo.adapter.repository.TaskRepository;
import com.roslabsystem.todo.adapter.repository.TodoRepository;
import com.roslabsystem.todo.adapter.web.dto.TodoAndTaskRequest;
import com.roslabsystem.todo.adapter.web.dto.TodoResponse;
import com.roslabsystem.todo.adapter.web.exceptions.NotFoundException;
import com.roslabsystem.todo.domain.TaskEntity;
import com.roslabsystem.todo.domain.TodoEntity;
import com.roslabsystem.todo.service.mapper.TaskMapper;
import com.roslabsystem.todo.service.mapper.TodoMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
        Optional<TodoEntity> todo = todoRepository.findByTodoName(todoAndTaskRequest.todoName());
        if (todo.isEmpty()) {
            throw new NotFoundException("todo");
        }

        TodoEntity todoEntity = todo.get();
        TaskEntity taskEntity = taskMapper.requestToEntity(todoAndTaskRequest);
        taskEntity.setTodo(todoEntity);
        TaskEntity saved = taskRepository.save(taskEntity);
        todoEntity.setTasks(saved);

        return todoMapper.entityToResponse(todoEntity);
    }


    public TodoResponse completeTaskById(Long id) {
        Optional<TaskEntity> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            throw new NotFoundException("task with this id");
        }

        taskRepository.updateTaskByIdSetIsCompletedToTrue(id);
        task.get().setIsCompleted(true);

        return todoMapper.entityToResponse(task.get().getTodo());
    }


    public TodoResponse unCompleteById(Long id) {
        Optional<TaskEntity> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            throw new NotFoundException("task with this id");
        }

        taskRepository.updateTaskByIdSetIsCompletedToFalse(id);
        task.get().setIsCompleted(false);

        return todoMapper.entityToResponse(task.get().getTodo());
    }

    public TodoResponse deleteById(Long id) {
        Optional<TaskEntity> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            throw new NotFoundException("task with this id");
        }

        taskRepository.deleteById(id);
        Long taskId = task.get().getTodo().getId();

        return todoMapper.entityToResponse(todoRepository.findById(taskId).get());
    }

    public TodoResponse deleteByRequest(TodoAndTaskRequest todoAndTaskRequest) {
        Optional<TodoEntity> todo = todoRepository.findByTodoName(todoAndTaskRequest.todoName());
        if (todo.isEmpty()) {
            throw new NotFoundException("todo");
        }

        Set<TaskEntity> tasks = todo.get().getTasks();
        Long id = null;
        for(TaskEntity taskEntity : tasks) {
            if (taskEntity.getDescription().equalsIgnoreCase(todoAndTaskRequest.taskName())) {
                id = taskEntity.getId();
            }
        }

        if (id == null) {
            throw new NotFoundException("task with this name");
        }

        taskRepository.deleteById(id);
        return todoMapper.entityToResponse(todoRepository.findByTodoName(todoAndTaskRequest.todoName()).get());
    }
}
