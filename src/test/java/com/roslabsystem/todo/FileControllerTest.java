package com.roslabsystem.todo;

import com.roslabsystem.todo.adapter.repository.FileRepository;
import com.roslabsystem.todo.adapter.repository.TaskRepository;
import com.roslabsystem.todo.adapter.repository.TodoRepository;
import com.roslabsystem.todo.adapter.repository.UserRepository;
import com.roslabsystem.todo.adapter.web.dto.response.FileResponse;
import com.roslabsystem.todo.adapter.web.exceptions.NotFoundException;
import com.roslabsystem.todo.domain.FileEntity;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {
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

    private TaskEntity task;
    @Autowired
    private FileRepository fileRepository;

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
        task = new TaskEntity(new TaskEntity.TaskContext("task"));
        todo.setUser(user);
        task.setTodo(todo);
        todo.getTasks().add(task);
        user.getTodos().add(todo);
        todoRepository.save(todo);
        taskRepository.save(task);
    }

    @AfterEach
    public void cleanUp() {
        fileRepository.deleteAll();
        taskRepository.deleteAll();
        todoRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("POST /file create file test returns ok status")
    @Test
    public void createFile_ReturnsValidResponse() throws Exception {
        String taskDescription = task.getDescription();
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello!".getBytes());
        MvcResult mvcResult = mockMvc.perform(multipart("http://localhost:8080/file")
                        .file(file)
                        .param("taskDescription", taskDescription)
                        .with(user(user))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is2xxSuccessful()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        FileResponse fileResponse = FileResponse.fromJson(content);
        FileEntity fileEntity = fileRepository.findByFileNameAndUser(fileResponse.name(), user).orElseThrow(() ->
                new NotFoundException(String.format("file with name: '%s' and user id: %s in file controller test", fileResponse.name(), user.getId())));

        assertThat(fileResponse.size()).isEqualTo(file.getSize());
        assertNotNull(fileEntity);
    }

    @DisplayName("POST /file create file test returns already exist exception")
    @Test
    public void createFile_ReturnsAlreadyExist() throws Exception {
        String taskDescription = task.getDescription();
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello!".getBytes());
        mockMvc.perform(multipart("http://localhost:8080/file")
                        .file(file)
                        .param("taskDescription", taskDescription)
                        .with(user(user))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is2xxSuccessful()).andReturn();

        mockMvc.perform(multipart("http://localhost:8080/file")
                        .file(file)
                        .param("taskDescription", taskDescription)
                        .with(user(user))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is4xxClientError()).andReturn();
    }

    @DisplayName("GET /file/{id} returns valid response")
    @Test
    public void getFileById_ReturnsValidResponseEntity() throws Exception {
        String taskDescription = task.getDescription();
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello!".getBytes());
        MvcResult mvcResult = mockMvc.perform(multipart("http://localhost:8080/file")
                        .file(file)
                        .param("taskDescription", taskDescription)
                        .with(user(user))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is2xxSuccessful()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        FileResponse fileResponse = FileResponse.fromJson(content);

        FileEntity fileEntity = fileRepository.findByIdAndUser(fileResponse.fileId(), user)
                .orElseThrow(() -> new NotFoundException(String.format("file with id: '%s' and user id: %s in file controller test", fileResponse.fileId(), user.getId())));

        mockMvc.perform(get("http://localhost:8080/file/{id}", fileEntity.getId())
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @DisplayName("DELETE file/{id} delete file by id returns valid response")
    @Test
    public void deleteTaskById_ReturnsValidResponse() throws Exception {
        String taskDescription = task.getDescription();
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello!".getBytes());
        MvcResult mvcResult = mockMvc.perform(multipart("http://localhost:8080/file")
                        .file(file)
                        .param("taskDescription", taskDescription)
                        .with(user(user))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is2xxSuccessful()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        FileResponse fileResponse = FileResponse.fromJson(content);

        mockMvc.perform(delete("http://localhost:8080/file/{id}", fileResponse.fileId())
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        Optional<FileEntity> fileEntity = fileRepository.findByIdAndUser(fileResponse.fileId(), user);
        assertTrue(fileEntity.isEmpty());
    }
}
