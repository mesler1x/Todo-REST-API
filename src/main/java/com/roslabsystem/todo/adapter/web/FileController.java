package com.roslabsystem.todo.adapter.web;

import com.roslabsystem.todo.adapter.web.dto.response.FileResponse;
import com.roslabsystem.todo.domain.user.UserEntity;
import com.roslabsystem.todo.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
@Tag(name = "Контроллер файла")
public class FileController {
    FileService fileService;

    @Operation(summary = "Сохраняет файл и прикрепляет его к задаче"
            , description = "Прикрепляет файл к задаче по имени задачи. Ограничение на файл - 5 МБ")
    @PostMapping
    public FileResponse uploadFileToTask(@AuthenticationPrincipal UserEntity user,
                                         @RequestParam String taskDescription,
                                         @NotNull(message = "file should not be null") @RequestParam("file") MultipartFile file) throws IOException {
        return fileService.store(user, taskDescription, file);
    }

    @Operation(summary = "Сохранение файла и прикрепление его к задаче",
            description = "Прикрепляет файл к задаче по id задачи. Ограничение на файл - 5 МБ")
    @PostMapping("/{id}")
    public FileResponse uploadFileToTaskById(@AuthenticationPrincipal UserEntity user,
                                             @PathVariable Long id,
                                             @NotNull(message = "file should not be null") @RequestParam("file") MultipartFile file) throws IOException {
        return fileService.store(user, id, file);
    }

    @Operation(summary = "Получение файла по его id",
            description = "Получение файла по его id. Для скачивания файла с сервера, перейдите в браузере по ссылке, указав id файла")
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFileById(@AuthenticationPrincipal UserEntity user,
                                              @NotNull(message = "id should not be null") @PathVariable Long id) {
        return fileService.getFile(user, id);
    }

    @Operation(summary = "Удаление файла по его id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFileById(@AuthenticationPrincipal UserEntity user,
                                            @PathVariable Long id) {
        return fileService.deleteById(user, id);
    }

    @Operation(summary = "Удаление файла по его имени")
    @DeleteMapping
    public ResponseEntity<?> deleteFileByFileName(@AuthenticationPrincipal UserEntity user,
                                                  @RequestParam String fileName) {
        return fileService.deleteByFileName(user, fileName);
    }

}
