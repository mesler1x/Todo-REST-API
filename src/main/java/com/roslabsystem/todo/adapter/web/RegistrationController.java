package com.roslabsystem.todo.adapter.web;

import com.roslabsystem.todo.adapter.web.dto.request.RegistrationRequest;
import com.roslabsystem.todo.adapter.web.dto.response.RegistrationResponse;
import com.roslabsystem.todo.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Validated
@RequiredArgsConstructor
public class RegistrationController {
    UserService userService;

    @PostMapping
    public RegistrationResponse registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return userService.registerUser(registrationRequest);
    }
}
