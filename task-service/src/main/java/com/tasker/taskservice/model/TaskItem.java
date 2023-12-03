package com.tasker.taskservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TaskItem {
    private Long id;
    private Long priority;
    private String title;
    private String description;
    private Date dateTime;
}
