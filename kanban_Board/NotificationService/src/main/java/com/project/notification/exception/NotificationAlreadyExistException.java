package com.project.notification.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Notification Already Exist")
public class NotificationAlreadyExistException extends Exception{
}
