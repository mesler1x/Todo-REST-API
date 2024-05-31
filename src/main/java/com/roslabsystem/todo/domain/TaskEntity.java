package com.roslabsystem.todo.domain;

import com.roslabsystem.todo.domain.common.BaseDomainEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "task")
public class TaskEntity extends BaseDomainEntity {
    String description;
    @ManyToOne
    @JoinColumn(name = "todo_id", referencedColumnName = "id")
    TodoEntity todo;
    Boolean isCompleted;
    @OneToMany(mappedBy = "task")
    List<FileEntity> file;

    public TaskEntity(TaskContext taskContext) {
        this.isCompleted = false;
        this.description = taskContext.description;
    }

    public record TaskContext(String description) {

    }
}
