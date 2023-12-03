package com.tasker.taskservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasker.taskservice.model.Task;
import com.tasker.taskservice.model.TaskGroup;
import com.tasker.taskservice.repository.TaskRepository;
import com.tasker.taskservice.response.GeneralResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskController controller;

    @Test
    void addTask() throws Exception {
        Task task = new Task();
        task.setTitle("Test Task");

        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.post("/task")
                        .content(asJsonString(task))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("Test Task"));
    }


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void findAll() {
        List<Task> mockTasks = createMockTasks();
        when(taskRepository.findAll()).thenReturn(mockTasks);

        ResponseEntity<List<TaskGroup>> responseEntity = controller.findAll();
        List<TaskGroup> taskGroups = responseEntity.getBody();

        assertEquals(4, taskGroups.size());

        verify(taskRepository, times(1)).findAll();
    }

    private List<Task> createMockTasks() {
        return List.of(
                new Task(452, 1, "Hello", "hi hi hi", "2023-12-03T21:24:20.523+00:00", "Todo"),
                new Task(106, 0, "Test5", "Test Description5", "2023-12-01T19:17:26.088+00:00", "In-progress"),
                new Task(152, 1, "Test8", "Test Description8", "2023-12-01T19:31:48.977+00:00", "In-progress"),
                new Task(253, 1, "Test102", "Test Description102", "2023-12-03T17:35:05.609+00:00", "Done")
        );
    }

    @Test
    void findById_ReturnsTask_Success() {
        // Arrange
        long taskId = 1L;
        Task mockTask = new Task((int) taskId, 1, "Test Task", "Description", "2023-12-03T21:24:20.523+00:00", "Todo");
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockTask));

        // Act
        ResponseEntity<GeneralResponse<Task>> responseEntity = controller.findById(taskId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("SUCCESS", responseEntity.getBody().getStatus());
        assertEquals(mockTask, responseEntity.getBody().getData());

        // Verify repository method is called
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void findById_TaskNotFound_ReturnsNotFound() {
        // Arrange
        long taskId = 2L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<GeneralResponse<Task>> responseEntity = controller.findById(taskId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("FAILED", responseEntity.getBody().getStatus());
        assertEquals("Not Found", responseEntity.getBody().getData());

        // Verify repository method is called
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void findById_InternalServerError_ReturnsInternalServerError() {
        // Arrange
        long taskId = 3L;
        when(taskRepository.findById(taskId)).thenThrow(new RuntimeException("Simulating Internal Server Error"));

        // Act
        ResponseEntity<GeneralResponse<Task>> responseEntity = controller.findById(taskId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("FAILED", responseEntity.getBody().getStatus());
        assertEquals("Internal Server Error", responseEntity.getBody().getData());

        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void deleteTask() throws Exception {
        Long taskId = 1L;

        when(taskRepository.existsById(taskId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/task/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.content().json("{\"status\":\"SUCCESS\",\"data\":\"Deleted\"}"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
