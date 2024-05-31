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
import jakarta.transaction.Transactional;
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
        TaskEntity task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("task with this id"));

        taskRepository.updateTaskByIdSetIsCompletedToTrue(id);
        task.setIsCompleted(true);

        return todoMapper.entityToResponse(task.getTodo());
    }


    public TodoResponse unCompleteById(Long id) {
        TaskEntity task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("task with this id"));

        taskRepository.updateTaskByIdSetIsCompletedToFalse(id);
        task.setIsCompleted(false);

        return todoMapper.entityToResponse(task.getTodo());
    }

    @Transactional
    public TodoResponse deleteById(Long id) {
        TaskEntity task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("task with this id"));

        taskRepository.deleteById(id);
        Long taskId = task.getTodo().getId();

        return todoMapper.entityToResponse(todoRepository.findById(taskId).get());
    }

    @Transactional
    public TodoResponse deleteByRequest(TodoAndTaskRequest todoAndTaskRequest) {
        TodoEntity todo = todoRepository.findByTodoName(todoAndTaskRequest.todoName()).orElseThrow(() -> new NotFoundException("todo"));

        Set<TaskEntity> tasks = todo.getTasks();
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
