package com.project.notification.controller;

import com.project.notification.domain.Task;
import com.project.notification.exception.NotificationAlreadyExistException;
import com.project.notification.service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("/api/v3")
public class NotificationController {
    private INotificationService notificationService;
    private ResponseEntity responseEntity;

    @Autowired
    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @PostMapping("/notification/save")
    public ResponseEntity<?> saveNotification(@RequestBody Task task) throws NotificationAlreadyExistException {
        try {
            responseEntity = new ResponseEntity<>(notificationService.saveNotification(task), HttpStatus.ACCEPTED);
        } catch (NotificationAlreadyExistException exception) {
            throw new NotificationAlreadyExistException();
        } catch (Exception exception) {
            responseEntity = new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping("/notification/all")
    public ResponseEntity<?> getAllMessages() {
        try {
            responseEntity = new ResponseEntity<>(notificationService.getAllMessages(), HttpStatus.OK);
        } catch (Exception exception) {
            responseEntity = new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}
