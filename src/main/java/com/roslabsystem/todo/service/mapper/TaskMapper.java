package com.roslabsystem.todo.service.mapper;

import com.roslabsystem.todo.adapter.web.dto.TaskResponse;
import com.roslabsystem.todo.adapter.web.dto.TodoAndTaskRequest;
import com.roslabsystem.todo.domain.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public TaskResponse entityToResponse(TaskEntity taskEntity) {
        return new TaskResponse(taskEntity.getId(), taskEntity.getDescription(), taskEntity.getIsCompleted());
    }

    public TaskEntity requestToEntity(TodoAndTaskRequest taskRequest) {
        TaskEntity.TaskContext context = new TaskEntity.TaskContext(taskRequest.taskName());
        return new TaskEntity(context);
    }
}
