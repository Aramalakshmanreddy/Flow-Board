package com.project.kanbanService.domain;

import org.springframework.data.annotation.Id;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Board {

    @Id
    private int boardId;
    private String boardName;
    private String description;
    private String creator;
    private String creatorEmail;
    private Date creationDate;
    private String[] members;
    private List<Stage> stages;

    public Board(int boardId, String boardName, String description, String creator, Date creationDate, String[] members, List<Stage> stages, String creatorEmail) {
        this.boardId = boardId;
        this.boardName = boardName;
        this.description = description;
        this.creator = creator;
        this.creationDate = creationDate;
        this.members = members;
        this.stages = stages;
        this.creatorEmail = creatorEmail;
    }

    public Board() {
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }
    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }
    @Override
    public String toString() {
        return "Board{" +
                "boardId=" + boardId +
                ", boardName='" + boardName + '\'' +
                ", description='" + description + '\'' +
                ", creator='" + creator + '\'' +
                ", creationDate=" + creationDate +
                ", members=" + Arrays.toString(members) +
                ", stages=" + stages +
                '}';
    }
}
