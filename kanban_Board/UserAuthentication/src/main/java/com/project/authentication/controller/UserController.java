package com.project.authentication.controller;

import com.project.authentication.domain.User;
import com.project.authentication.exception.InvalidCredentialsException;
import com.project.authentication.exception.UserAlreadyExistException;
import com.project.authentication.repository.UserRepository;
import com.project.authentication.security.ISecurityTokenGenerate;
import com.project.authentication.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("/api/v1")
public class UserController
{
    private ISecurityTokenGenerate iSecurityTokenGenerate;
    private IUserService iUserService;
    private ResponseEntity responseEntity;

    @Autowired
    public UserController(ISecurityTokenGenerate iSecurityTokenGenerate, IUserService iUserService) {
        this.iSecurityTokenGenerate = iSecurityTokenGenerate;
        this.iUserService = iUserService;
    }
    @PostMapping("/user/save")
    public ResponseEntity<?> saveUser(@RequestBody User user) throws UserAlreadyExistException
    {
        try {
            responseEntity = new ResponseEntity<>(iUserService.registerUser(user), HttpStatus.CREATED);
        } catch (UserAlreadyExistException ex) {
            throw new UserAlreadyExistException();
        } catch (Exception ex) {
            responseEntity = new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }
    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody User user) throws InvalidCredentialsException
    {
        try {
            User  retrievedUser = iUserService.loginUser(user.getEmail(),user.getPassword());
            responseEntity= new  ResponseEntity<>(iSecurityTokenGenerate.createToken(retrievedUser),HttpStatus.CREATED);
        } catch (InvalidCredentialsException ex) {
            throw new InvalidCredentialsException();
        } catch (Exception ex) {
            responseEntity = new ResponseEntity<>("Try after sometime!!!",HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }
    @DeleteMapping("/user/delete")
    public ResponseEntity<?> deleteUser(@RequestBody User user) throws InvalidCredentialsException
    {
        try {
            iUserService.deleteUser(user);
            responseEntity = new ResponseEntity<>("user deleted", HttpStatus.OK);
        } catch (InvalidCredentialsException ex) {
            throw new InvalidCredentialsException();
        } catch (Exception ex ) {
            responseEntity = new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }
}
