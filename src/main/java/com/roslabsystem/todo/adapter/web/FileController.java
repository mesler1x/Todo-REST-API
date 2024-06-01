package com.roslabsystem.todo.adapter.web;

import com.roslabsystem.todo.adapter.web.dto.request.FileCreateRequest;
import com.roslabsystem.todo.adapter.web.dto.response.FileResponse;
import com.roslabsystem.todo.domain.user.UserEntity;
import com.roslabsystem.todo.service.FileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Validated
@RequiredArgsConstructor
public class FileController {
    FileService fileService;

    @PostMapping
    public FileResponse uploadFileToTask(@AuthenticationPrincipal UserEntity user,
                                         @RequestParam String taskDescription,
                                         @NotNull(message = "file should not be null") @RequestParam("file") MultipartFile file) throws IOException {
        return fileService.store(user, taskDescription, file);
    }

    @PostMapping("/{id}")
    public FileResponse uploadFileToTaskById(@AuthenticationPrincipal UserEntity user,
                                             @PathVariable Long id,
                                             @NotNull(message = "file should not be null") @RequestParam("file") MultipartFile file) throws IOException {
        return fileService.store(user, id, file);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFileById(@AuthenticationPrincipal UserEntity user,
                                              @NotNull(message = "id should not be null") @PathVariable Long id) {
        return fileService.getFile(user, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFileById(@AuthenticationPrincipal UserEntity user,
                                            @PathVariable Long id) {
        return fileService.deleteById(user, id);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFileByFileName(@AuthenticationPrincipal UserEntity user,
                                                  @RequestParam String fileName) {
        return fileService.deleteByFileName(user, fileName);
    }

}
