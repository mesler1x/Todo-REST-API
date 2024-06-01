package com.roslabsystem.todo.service.mapper;

import com.roslabsystem.todo.adapter.web.dto.response.FileResponse;
import com.roslabsystem.todo.domain.FileEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class FileMapper {
    public FileResponse entityToResponse(FileEntity file) {
        String fileURI = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/file")
                .path(String.valueOf(file.getId()))
                .toUriString();
        return new FileResponse(
                file.getId(),
                file.getName(),
                fileURI,
                file.getType(),
                file.getData().length,
                file.getTask().getId()
        );
    }
}
