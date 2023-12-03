package com.tasker.taskservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TaskGroup {
    private String name;
    private List<TaskItem> items;
}
