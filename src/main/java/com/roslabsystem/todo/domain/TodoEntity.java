package com.roslabsystem.todo.domain;

import com.roslabsystem.todo.domain.common.BaseDomainEntity;
import com.roslabsystem.todo.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "todo")
@Setter
public class TodoEntity extends BaseDomainEntity {
    String todoName;
    @OneToMany(mappedBy = "todo", fetch = FetchType.EAGER)
    Set<TaskEntity> tasks;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    UserEntity user;

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
