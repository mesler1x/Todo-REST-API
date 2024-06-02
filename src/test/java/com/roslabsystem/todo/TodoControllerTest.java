package com.roslabsystem.todo;

import com.roslabsystem.todo.adapter.repository.TodoRepository;
import com.roslabsystem.todo.adapter.repository.UserRepository;
import com.roslabsystem.todo.adapter.web.dto.request.TodoRequest;
import com.roslabsystem.todo.adapter.web.dto.response.TodoResponse;
import com.roslabsystem.todo.adapter.web.exceptions.NotFoundException;
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
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    UserRepository userRepository;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13.3"));

    private UserEntity user;

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
    }

    @AfterEach
    public void cleanUp() {
        todoRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("POST /todo create todo and returns ok status")
    @Test
    public void createTodo_ReturnsValidResponse() throws Exception {
        TodoRequest todoRequest = new TodoRequest("todo");

        mockMvc.perform(post("http://localhost:8080/todo")
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(todoRequest)))
                .andExpect(status().is2xxSuccessful());

        TodoEntity todo = todoRepository.findByTodoNameAndUser(todoRequest.todoName(), user)
                .orElseThrow(() -> new NotFoundException(String.format("todo with name: '%s' and user id: %s",
                        todoRequest.todoName(), user.getId())));

        assertThat(todo).isNotNull();
        assertThat(todo.getTodoName()).isEqualTo(todoRequest.todoName());
        assertThat(todo.getUser().getUsername()).isEqualTo(user.getUsername());
    }

    @DisplayName("GET /todo/{id} get todo by id and returns ok status")
    @Test
    public void getTodoById_ReturnsValidResponse() throws Exception {
        TodoRequest todoRequest = new TodoRequest("todo");

        TodoEntity todoEntity = new TodoEntity(todoRequest.todoName(), new HashSet<>(), user);

        TodoEntity todo = todoRepository.save(todoEntity);
        user.getTodos().add(todo);

        MvcResult mvcResult = mockMvc.perform(get("http://localhost:8080/todo/{id}", todo.getId())
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        TodoResponse todoResponse = TodoResponse.fromJson(content);

        assertThat(todoResponse).isNotNull();
        assertThat(todoResponse.todoName()).isEqualTo(todoRequest.todoName());
    }

    @DisplayName("DELETE /todo/{id} delete todo by id and returns ok status")
    @Test
    public void deleteTodo_ReturnsValidResponseEntity() throws Exception {
        TodoRequest todoRequest = new TodoRequest("todo");

        TodoEntity todoEntity = new TodoEntity(todoRequest.todoName(), new HashSet<>(), user);

        TodoEntity todo = todoRepository.save(todoEntity);
        user.getTodos().add(todo);

        mockMvc.perform(delete("http://localhost:8080/todo/{id}", todo.getId())
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();

        Optional<TodoEntity> optional = todoRepository.findByIdAndUser(todo.getId(), user);
        assertTrue(optional.isEmpty());
    }
}
