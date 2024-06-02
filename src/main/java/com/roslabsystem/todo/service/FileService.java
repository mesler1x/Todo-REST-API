package com.roslabsystem.todo.service;

import com.roslabsystem.todo.adapter.repository.FileRepository;
import com.roslabsystem.todo.adapter.repository.TaskRepository;
import com.roslabsystem.todo.adapter.web.dto.response.FileResponse;
import com.roslabsystem.todo.adapter.web.exceptions.AlreadyExistException;
import com.roslabsystem.todo.adapter.web.exceptions.NotFoundException;
import com.roslabsystem.todo.domain.FileEntity;
import com.roslabsystem.todo.domain.TaskEntity;
import com.roslabsystem.todo.domain.user.UserEntity;
import com.roslabsystem.todo.service.mapper.FileMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileService {
    FileRepository fileRepository;
    TaskRepository taskRepository;
    FileMapper fileMapper;

    public FileResponse store(UserEntity user, String taskDescription, MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        TaskEntity task = taskRepository.findByDescriptionAndUser(taskDescription, user)
                .orElseThrow(() -> new NotFoundException(String.format("task with description: '%s' and user with id: %s",
                        taskDescription, user.getId())));

        fileRepository.findByFileNameAndUser(fileName, user)
                .ifPresent(existingFileName -> {
                    throw new AlreadyExistException(String.format("file with filename: '%s'", fileName));
                });

        FileEntity fileEntity = new FileEntity(fileName, file.getContentType(), file.getBytes(), task);
        fileEntity.setTask(task);

        return fileMapper.entityToResponse(fileRepository.save(fileEntity));
    }

    public ResponseEntity<byte[]> getFile(UserEntity user, Long id) {
        FileEntity fileEntity = fileRepository.findByIdAndUser(id, user).orElseThrow(() ->
                new NotFoundException(String.format("file with id: %s and user with id: %s", id, user.getId())));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(fileEntity.getName()))
                .body(fileEntity.getData());
    }

    public FileResponse store(UserEntity user, Long id, MultipartFile file) throws IOException {
        TaskEntity task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new NotFoundException(String.format("task with id: %s and user with id: %s",
                        id, user.getId())));

        return store(user, task.getDescription(), file);
    }

    public ResponseEntity<?> deleteById(UserEntity user, Long id) {
        fileRepository.findByIdAndUser(id, user).orElseThrow(() ->
                new NotFoundException(String.format("file with id: %s and user with id: %s", id, user.getId())));
        fileRepository.deleteByIdAndUser(id, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteByFileName(UserEntity user, String fileName) {
        fileRepository.findByFileNameAndUser(fileName, user).orElseThrow(() ->
                new NotFoundException(String.format("file with name: '%s' and user with id: %s", fileName, user.getId())));
        fileRepository.deleteByFileNameAndUser(fileName, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
