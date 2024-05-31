package com.roslabsystem.todo.adapter.repository;

import com.roslabsystem.todo.domain.TaskEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE TaskEntity t SET t.isCompleted = true WHERE t.id = :id")
    void updateTaskByIdSetIsCompletedToTrue(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE TaskEntity t SET t.isCompleted = false WHERE t.id = :id")
    void updateTaskByIdSetIsCompletedToFalse(@Param("id") Long id);
}
