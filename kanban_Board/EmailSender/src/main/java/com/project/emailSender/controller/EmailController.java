package com.project.emailSender.controller;

import com.project.emailSender.domain.Email;
import com.project.emailSender.domain.User;
import com.project.emailSender.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v4")
public class EmailController {
    private final IEmailService emailService;
    private ResponseEntity responseEntity;

    @Autowired
    public EmailController(IEmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/confirm/creator")
    public ResponseEntity<?> confirmRegisterForCreator(@RequestBody User user) {
        try {
            responseEntity = new ResponseEntity<>(emailService.confirmRegisterForCreator(user), HttpStatus.OK);
        } catch (Exception exception) {
            responseEntity = new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println(responseEntity);
        return responseEntity;
    }
    @PostMapping("/confirm/member")
    public ResponseEntity<?> confirmRegisterForMember(@RequestBody User user) {
        try {
            responseEntity = new ResponseEntity<>(emailService.confirmRegisterForMember(user), HttpStatus.OK);
        } catch (Exception exception) {
            responseEntity = new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println(responseEntity);
        return responseEntity;
    }
    @PostMapping("/remove/member")
    public ResponseEntity<?> memberRemovedConfirmation(@RequestBody User user) {
        try {
            responseEntity = new ResponseEntity<>(emailService.memberRemovedConfirmation(user), HttpStatus.OK);
        } catch (Exception exception) {
            responseEntity = new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println(responseEntity);
        return responseEntity;
    }
    @PostMapping("/confirm/task/completed")
    public ResponseEntity<?> completedTaskConfirmation(@RequestParam String creatorEmail, @RequestParam String taskMessage) {
        try {
            responseEntity = new ResponseEntity<>(emailService.completedTaskConfirmation(creatorEmail, taskMessage), HttpStatus.OK);
        } catch (Exception exception) {
            responseEntity = new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println(responseEntity);
        return responseEntity;
    }
}
