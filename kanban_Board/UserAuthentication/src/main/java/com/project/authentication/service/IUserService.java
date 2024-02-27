package com.project.authentication.service;

import com.project.authentication.domain.User;
import com.project.authentication.exception.InvalidCredentialsException;
import com.project.authentication.exception.UserAlreadyExistException;

public interface IUserService
{
    public User registerUser(User user) throws UserAlreadyExistException;
    public User loginUser(String email, String password) throws InvalidCredentialsException;
    public void deleteUser(User user) throws InvalidCredentialsException;
}
