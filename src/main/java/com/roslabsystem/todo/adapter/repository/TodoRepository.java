package com.roslabsystem.todo.adapter.repository;

import com.roslabsystem.todo.domain.TodoEntity;
import com.roslabsystem.todo.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    @Query("SELECT t FROM TodoEntity t WHERE t.todoName = :todoName AND t.user = :user")
    Optional<TodoEntity> findByTodoNameAndUser(@Param("todoName") String todoName, @Param("user") UserEntity user);
    Optional<TodoEntity> findByIdAndUser(Long id, UserEntity user);
}
