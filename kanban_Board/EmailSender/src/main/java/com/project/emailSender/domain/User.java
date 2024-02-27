package com.project.emailSender.domain;

public class User
{
    private String email;
    private String userName;
    private String password;
    private String creatorEmail;

    public User(String email, String userName, String password, String creatorEmail) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.creatorEmail = creatorEmail;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", creatorEmail='" + creatorEmail + '\'' +
                '}';
    }
}
