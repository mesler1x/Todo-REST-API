package com.roslabsystem.todo.service.mapper;

import com.roslabsystem.todo.adapter.web.dto.request.TodoRequest;
import com.roslabsystem.todo.adapter.web.dto.response.TodoResponse;
import com.roslabsystem.todo.domain.TodoEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TodoMapper {
    TaskMapper taskMapper;

    public TodoEntity requestToEntity(TodoRequest todoRequest) {
        TodoEntity.TodoContext context = new TodoEntity.TodoContext(todoRequest.todoName());
        return new TodoEntity(context);
    }

    public TodoResponse entityToResponse(TodoEntity todoEntity) {
        return new TodoResponse(todoEntity.getId(), todoEntity.getTodoName(),
                todoEntity.getTasks().stream().map(taskMapper::entityToResponse).toList());
    }
}