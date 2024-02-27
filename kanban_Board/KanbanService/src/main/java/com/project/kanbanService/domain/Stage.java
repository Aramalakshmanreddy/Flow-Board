package com.project.kanbanService.domain;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Stage {

    @Id
    private int stageId;
    private String stageName;
    private List<Task> tasks;

    public Stage(int stageId, String stageName, List<Task> tasks) {
        this.stageId = stageId;
        this.stageName = stageName;
        this.tasks = tasks;
    }

    public Stage(int stageId, String stageName) {
        this.stageId = stageId;
        this.stageName = stageName;
    }

    public Stage() {
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "Stage{" +
                "stageId=" + stageId +
                ", stageName='" + stageName + '\'' +
                ", tasks=" + tasks +
                '}';
    }
}
