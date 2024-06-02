package com.roslabsystem.todo;

import com.roslabsystem.todo.adapter.repository.TaskRepository;
import com.roslabsystem.todo.adapter.repository.TodoRepository;
import com.roslabsystem.todo.adapter.repository.UserRepository;
import com.roslabsystem.todo.adapter.web.dto.request.TodoAndTaskRequest;
import com.roslabsystem.todo.adapter.web.dto.request.UpdateTaskRequest;
import com.roslabsystem.todo.adapter.web.exceptions.NotFoundException;
import com.roslabsystem.todo.domain.TaskEntity;
import com.roslabsystem.todo.domain.TodoEntity;
import com.roslabsystem.todo.domain.user.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13.3"));

    private UserEntity user;

    private TodoEntity todo;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }


    @BeforeEach
    public void setUpUser() {
        user = UserEntity.from(new UserEntity.Context("sample", "$2a$10$eJUvMMzj86BxPuzo/XZSjeQY96U6wm0pqmvlWuieFN4DE0RupJoJO"));
        userRepository.save(user);
        todo = new TodoEntity(new TodoEntity.TodoContext("todo"));
        todo.setUser(user);
        user.getTodos().add(todo);
        todoRepository.save(todo);
    }

    @AfterEach
    public void cleanUp() {
        taskRepository.deleteAll();
        todoRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("POST /task update task to completed and returns ok status")
    @Test
    public void createTask_ReturnsValidResponse() throws Exception {
        TodoAndTaskRequest todoAndTaskRequest = new TodoAndTaskRequest("todo", "task");

        mockMvc.perform(post("http://localhost:8080/task")
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(todoAndTaskRequest)))
                .andExpect(status().is2xxSuccessful());

        TaskEntity task = taskRepository.findByDescriptionAndUser(todoAndTaskRequest.taskDescription(), user)
                .orElseThrow(() ->
                        new NotFoundException(String.format("task with description: '%s' and user id: %s",
                                todoAndTaskRequest.taskDescription(), user.getId())));

        assertThat(task).isNotNull();
        assertThat(task.getDescription()).isEqualTo(todoAndTaskRequest.taskDescription());
        assertThat(task.getTodo().getUser().getUsername()).isEqualTo(user.getUsername());
    }

    @DisplayName("PATCH /task returns update task to true and returns ok status")
    @Test
    public void updateTaskIsCompletedFlagToTrue_ReturnsValidResponse() throws Exception {
        TodoAndTaskRequest todoAndTaskRequest = new TodoAndTaskRequest("todo", "task");
        mockMvc.perform(post("http://localhost:8080/task")
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(todoAndTaskRequest)))
                .andExpect(status().is2xxSuccessful());

        TaskEntity task = taskRepository.findByDescriptionAndUser(todoAndTaskRequest.taskDescription(), user)
                .orElseThrow(() -> new NotFoundException("test"));

        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest(task.getId(), task.getDescription(), true);
        mockMvc.perform(patch("http://localhost:8080/task").with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateTaskRequest)))
                .andExpect(status().is2xxSuccessful());

        TaskEntity taskEntity = taskRepository.findByIdAndUser(task.getId(), user)
                .orElseThrow(() -> new NotFoundException(String.format("task with id: %s and user id: %s",
                        task.getId(), user.getId())));

        assertThat(taskEntity).isNotNull();
        assertTrue(taskEntity.getIsCompleted());
    }

    @DisplayName("PATCH /task update task to false and returns ok status")
    @Test
    public void updateTaskIsCompletedFlagToFalse_ReturnsValidResponse() throws Exception {
        TodoAndTaskRequest todoAndTaskRequest = new TodoAndTaskRequest("todo", "task");
        mockMvc.perform(post("http://localhost:8080/task")
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(todoAndTaskRequest)))
                .andExpect(status().is2xxSuccessful());

        TaskEntity task = taskRepository.findByDescriptionAndUser(todoAndTaskRequest.taskDescription(), user)
                .orElseThrow(() -> new NotFoundException("test"));

        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest(task.getId(), task.getDescription(), false);
        mockMvc.perform(patch("http://localhost:8080/task").with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateTaskRequest)))
                .andExpect(status().is2xxSuccessful());

        TaskEntity taskEntity = taskRepository.findByIdAndUser(task.getId(), user)
                .orElseThrow(() -> new NotFoundException(String.format("task with id: %s and user id: %s",
                        task.getId(), user.getId())));

        assertThat(taskEntity).isNotNull();
        assertFalse(taskEntity.getIsCompleted());
    }

    @DisplayName("PATCH /task update task description and returns ok status")
    @Test
    public void updateTaskDescription_ReturnsValidResponse() throws Exception {
        TodoAndTaskRequest todoAndTaskRequest = new TodoAndTaskRequest("todo", "task");
        mockMvc.perform(post("http://localhost:8080/task")
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(todoAndTaskRequest)))
                .andExpect(status().is2xxSuccessful());

        TaskEntity task = taskRepository.findByDescriptionAndUser(todoAndTaskRequest.taskDescription(), user)
                .orElseThrow(() -> new NotFoundException("test"));

        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest(task.getId(), "new description", false);

        mockMvc.perform(patch("http://localhost:8080/task").with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateTaskRequest)))
                .andExpect(status().is2xxSuccessful());

        TaskEntity taskEntity = taskRepository.findByIdAndUser(task.getId(), user)
                .orElseThrow(() -> new NotFoundException(String.format("task with id: %s and user id: %s",
                        task.getId(), user.getId())));

        assertThat(taskEntity).isNotNull();
        assertFalse(taskEntity.getIsCompleted());
        assertThat(taskEntity.getDescription()).isNotEqualTo(task.getDescription());
    }

    @DisplayName("DELETE /task/{id} delete task and returns ok status")
    @Test
    public void deleteTaskById_ReturnsValidResponse() throws Exception {
        TodoAndTaskRequest todoAndTaskRequest = new TodoAndTaskRequest("todo", "task");
        mockMvc.perform(post("http://localhost:8080/task")
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(todoAndTaskRequest)))
                .andExpect(status().is2xxSuccessful());

        TaskEntity task = taskRepository.findByDescriptionAndUser(todoAndTaskRequest.taskDescription(), user)
                .orElseThrow(() -> new NotFoundException(String.format("task with description: '%s' and user id: %s",
                        todoAndTaskRequest.taskDescription(), user.getId())));

        mockMvc.perform(delete("http://localhost:8080/task/{id}", task.getId())
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(todoAndTaskRequest)))
                .andExpect(status().is2xxSuccessful());

        Optional<TaskEntity> optionalTaskEntity = taskRepository.findByIdAndUser(task.getId(), user);

        assertTrue(optionalTaskEntity.isEmpty());
    }
}
