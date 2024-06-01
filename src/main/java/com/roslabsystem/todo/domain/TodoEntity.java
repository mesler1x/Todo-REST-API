package com.roslabsystem.todo.domain;

import com.roslabsystem.todo.domain.common.BaseDomainEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "todo")
public class TodoEntity extends BaseDomainEntity {
    String todoName;
    @OneToMany(mappedBy = "todo", fetch = FetchType.EAGER)
    Set<TaskEntity> tasks;

    public TodoEntity(TodoContext todoContext) {
        todoName = todoContext.todoName;
        tasks = new HashSet<>();
    }

    public record TodoContext(String todoName) {
    }

    public void addTask(TaskEntity task) {
        this.tasks.add(task);
    }
}
