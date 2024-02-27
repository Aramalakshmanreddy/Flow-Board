package com.project.kanbanService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND , reason="Tasks Not Found")
public class TaskNotFoundException extends Exception{
}
