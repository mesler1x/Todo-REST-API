package com.roslabsystem.todo.adapter.web.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record FileCreateRequest(@NotNull(message = "description should not be null")
                                @NotBlank(message = "description should not be blank")
                                String taskDescription,
                                @NotNull(message = "file should not be null")
                                MultipartFile multipartFile) {
}
