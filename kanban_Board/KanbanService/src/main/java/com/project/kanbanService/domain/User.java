package com.project.kanbanService.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class User {
    @Id
    private String email;
    private String userName;
    //private String creatorEmail;
    private List<Board> boards;

    public User(String email, String userName, List<Board> boards) {
        this.email = email;
        this.userName = userName;
        this.boards = boards;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Board> getBoards() {
        return boards;
    }

    public void setBoards(List<Board> boards) {
        this.boards = boards;
    }

//    public String getCreatorEmail() {
//        return creatorEmail;
//    }
//
//    public void setCreatorEmail(String creatorEmail) {
//        this.creatorEmail = creatorEmail;
//    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", userName='" + userName + '\'' +
               // ", creatorEmail='" + creatorEmail + '\'' +
                ", boards=" + boards +
                '}';
    }
}
