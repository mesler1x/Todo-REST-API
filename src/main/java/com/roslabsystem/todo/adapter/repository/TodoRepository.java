package com.roslabsystem.todo.adapter.repository;

import com.roslabsystem.todo.domain.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    Optional<TodoEntity> findByTodoName(String todoName);
}