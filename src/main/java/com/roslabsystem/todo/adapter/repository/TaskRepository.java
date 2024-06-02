package com.roslabsystem.todo.adapter.repository;

import com.roslabsystem.todo.domain.TaskEntity;
import com.roslabsystem.todo.domain.user.UserEntity;
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
    @Query("UPDATE TaskEntity t SET t.isCompleted = :isCompleted, t.description = :description WHERE t.id = :id")
    void updateTaskById(@Param("id") Long id,
                        @Param("isCompleted") Boolean isCompleted,
                        @Param("description") String description);

    @Query("SELECT task FROM TaskEntity task JOIN task.todo todo JOIN todo.user user WHERE task.id = :id AND user = :user")
    Optional<TaskEntity> findByIdAndUser(@Param("id") Long id,
                                         @Param("user") UserEntity user);

    @Modifying
    @Transactional
    @Query("DELETE FROM TaskEntity task WHERE task.id = :id AND task.todo.user = :user")
    void deleteByIdAndUser(@Param("id") Long id, @Param("user") UserEntity user);

    @Query("SELECT task FROM TaskEntity task WHERE task.description = :description AND task.todo.user = :user")
    Optional<TaskEntity> findByDescriptionAndUser(@Param("description") String description, @Param("user") UserEntity user);
}
