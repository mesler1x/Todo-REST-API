package com.roslabsystem.todo.adapter.repository;

import com.roslabsystem.todo.domain.FileEntity;
import com.roslabsystem.todo.domain.user.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    @Transactional
    @Query("SELECT f from FileEntity f WHERE f.task.todo.user = :user AND f.id = :id")
    Optional<FileEntity> findByIdAndUser(@Param("id") Long id, @Param("user") UserEntity user);

    @Transactional
    @Modifying
    @Query("DELETE FROM FileEntity file WHERE exists(SELECT 1 FROM TaskEntity task JOIN task.todo todo JOIN todo.user user WHERE user = :user) AND file.id = :id")
    void deleteByIdAndUser(@Param("id") Long id, @Param("user") UserEntity user);

    @Transactional
    @Query("SELECT f FROM FileEntity f WHERE f.task.todo.user = :user AND f.name = :fileName")
    Optional<FileEntity> findByFileNameAndUser(@Param("fileName") String fileName, @Param("user") UserEntity user);

    @Transactional
    @Modifying
    @Query("DELETE FROM FileEntity f WHERE exists(SELECT 1 FROM TaskEntity task JOIN task.todo todo JOIN todo.user user WHERE user = :user) AND f.name = :fileName")
    void deleteByFileNameAndUser(@Param("fileName") String fileName, @Param("user") UserEntity user);
}
