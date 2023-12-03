package com.tasker.taskservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity(name = "tbl_task")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "task_id")
    private Long taskId;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "date_time")
    private Date dateTime;
    @Column(name = "status")
    private String status;
    @Column(name = "priority")
    private Long priority;

    public Task(int i, int i1, String hello, String hi_hi_hi, String s, String toDo) {
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dateTime=" + dateTime +
                ", status='" + status + '\'' +
                '}';
    }

    public Task() {
        this.dateTime = new Date();
    }

    public Task(Long id, String title) {
        this.taskId = id;
        this.title = title;
        this.dateTime = new Date();
    }

    public Long getId() {
        return taskId;
    }

    public void setId(Long id) {
        this.taskId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }
}
