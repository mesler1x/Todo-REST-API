package com.roslabsystem.todo.adapter.repository;

import com.roslabsystem.todo.domain.TaskEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE TaskEntity t SET t.isCompleted = :isCompleted WHERE t.id = :id")
    void updateTaskByIdSetIsCompletedToTrue(@Param("id") Long id, @Param("isCompleted") Boolean isCompleted);

    Optional<TaskEntity> findByDescription(String description);
}
