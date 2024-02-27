package com.project.notification.domain;

import java.util.Date;

public class Task
{
    private int taskId;
    private String taskName;
    private String description;
    private String assignee;
    private Date dueDate;
    private String priority;

    public Task() {
    }

    public Task(int taskId, String taskName, String description, String assignee, Date dueDate, String priority) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description;
        this.assignee = assignee;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", assignee='" + assignee + '\'' +
                ", dueDate=" + dueDate +
                ", priority='" + priority + '\'' +
                '}';
    }
}
