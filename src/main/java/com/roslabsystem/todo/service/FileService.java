package com.roslabsystem.todo.service;

import com.roslabsystem.todo.adapter.repository.FileRepository;
import com.roslabsystem.todo.adapter.web.exceptions.NotFoundException;
import com.roslabsystem.todo.domain.FileEntity;
import com.roslabsystem.todo.domain.TaskEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileService {
    FileRepository fileRepository;

    public FileEntity store(TaskEntity task, MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        FileEntity fileEntity = new FileEntity(fileName, file.getContentType(), file.getBytes(), task);
        task.getFile().add(fileEntity);
        return fileRepository.save(fileEntity);
    }

    public FileEntity getFile(Long id) {
        return fileRepository.findById(id).orElseThrow(() -> new NotFoundException("file with id: " + id));
    }

    public Stream<FileEntity> getAllFiles() {
        return fileRepository.findAll().stream();
    }
}
