package com.tasker.taskservice.model;

import jakarta.persistence.*;

@Entity
public class TaskTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Override
    public String toString() {
        return "TaskTest{" +
                "taskId=" + taskId +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    private String title;
    private String status;

    public TaskTest() {
    }

    public TaskTest(Long taskId, String title, String status) {
        this.taskId = taskId;
        this.title = title;
        this.status = status;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
