package com.roslabsystem.todo.adapter.repository;

import com.roslabsystem.todo.domain.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
