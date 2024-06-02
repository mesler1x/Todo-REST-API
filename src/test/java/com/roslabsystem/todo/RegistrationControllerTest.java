package com.roslabsystem.todo;

import com.roslabsystem.todo.adapter.repository.UserRepository;
import com.roslabsystem.todo.adapter.web.dto.request.RegistrationRequest;
import com.roslabsystem.todo.adapter.web.exceptions.NotFoundException;
import com.roslabsystem.todo.domain.user.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13.3"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }


    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /registration register valid user and returns ok status")
    public void registrationTest_RegisterValidUserAndReturnsOkStatus() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest("sample", "sample");
        mockMvc.perform(post("http://localhost:8080/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationRequest)))
                .andExpect(status().is2xxSuccessful());

        UserEntity user = userRepository.findByUsername(registrationRequest.username()).orElseThrow(() ->
                new NotFoundException(String.format("user with username: '%s' in registration controller test", registrationRequest.username())));

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(registrationRequest.username());
    }

    @Test
    @DisplayName("POST /registration returns already exist exception")
    public void registrationTest_ReturnsAlreadyExistException() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest("sample", "sample");
        mockMvc.perform(post("http://localhost:8080/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationRequest)))
                .andExpect(status().is2xxSuccessful());
        mockMvc.perform(post("http://localhost:8080/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationRequest)))
                .andExpect(status().is4xxClientError());

        UserEntity user = userRepository.findByUsername(registrationRequest.username()).orElseThrow(() ->
                new NotFoundException(String.format("user with username: '%s' in registration controller test", registrationRequest.username())));

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(registrationRequest.username());
    }

}
