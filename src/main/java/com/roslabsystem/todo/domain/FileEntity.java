package com.roslabsystem.todo.domain;


import com.roslabsystem.todo.domain.common.BaseDomainEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "file")
public class FileEntity extends BaseDomainEntity {
    String name;
    String type;
    @Lob
    private byte[] data;
    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    TaskEntity task;
}
