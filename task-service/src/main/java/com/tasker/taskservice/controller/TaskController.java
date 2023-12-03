package com.tasker.taskservice.controller;

import com.tasker.taskservice.model.Task;
import com.tasker.taskservice.model.TaskGroup;
import com.tasker.taskservice.model.TaskItem;
import com.tasker.taskservice.repository.TaskRepository;
import com.tasker.taskservice.response.GeneralResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskRepository repository;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public ResponseEntity<GeneralResponse<Task>> add(@RequestBody Task task) {
        try {
            LOGGER.info("Task add: {}", task);
            Task savedTask = repository.save(task);
            return new ResponseEntity<>(new GeneralResponse<>("SUCCESS", savedTask), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(new GeneralResponse<>("FAILED", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    public ResponseEntity<List<TaskGroup>> findAll() {
        try {
            List<Task> tasks = repository.findAll();

            Map<String, String> statusMapping = Map.of(
                    "ToDo", "Todo",
                    "In-progress", "In-progress",
                    "In-review", "In-review",
                    "Done", "Done"
            );

            List<String> statusOrder = List.of("Todo", "In-progress", "In-review", "Done");

            Map<String, List<Task>> groupedTasks = tasks.stream()
                    .collect(Collectors.groupingBy(task -> statusMapping.get(task.getStatus())));

            List<TaskGroup> taskGroups = statusOrder.stream()
                    .filter(groupedTasks::containsKey)
                    .map(status -> {
                        List<Task> taskList = groupedTasks.get(status);

                        List<TaskItem> taskItems = taskList.stream()
                                .map(task -> new TaskItem(
                                        task.getTaskId(),
                                        task.getPriority(),
                                        task.getTitle(),
                                        task.getDescription(),
                                        task.getDateTime()
                                ))
                                .collect(Collectors.toList());

                        return new TaskGroup(status, taskItems);
                    })
                    .collect(Collectors.toList());

            return new ResponseEntity<>(taskGroups, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(List.of(new TaskGroup("FAILED", List.of(new TaskItem(null, null, "Internal Server Error", null, null)))), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse<Task>> findById(@PathVariable Long id) {
        try {
            LOGGER.info("Tasks Find by ID: {}", id);
            Optional<Task> task = repository.findById(id);

            return task.map(value -> new ResponseEntity<>(new GeneralResponse<>("SUCCESS", value), HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity(new GeneralResponse<>("FAILED", "Not Found"), HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity(new GeneralResponse<>("FAILED", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse<Task>> update(@PathVariable Long id, @RequestBody Task updatedTask) {
        try {
            LOGGER.info("Updating Task with ID {}: {}", id, updatedTask);
            if (repository.existsById(id)) {
                updatedTask.setId(id);
                Task savedTask = repository.save(updatedTask);
                return new ResponseEntity<>(new GeneralResponse<>("SUCCESS", savedTask), HttpStatus.OK);
            } else {
                return new ResponseEntity(new GeneralResponse<>("FAILED", "Not Found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity(new GeneralResponse<>("FAILED", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse<Void>> delete(@PathVariable Long id) {
        try {
            LOGGER.info("Deleting Task with ID: {}", id);
            if (repository.existsById(id)) {
                repository.deleteById(id);
                return new ResponseEntity(new GeneralResponse<>("SUCCESS", "Deleted"), HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity(new GeneralResponse<>("FAILED", "Not Found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity(new GeneralResponse<>("FAILED", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
