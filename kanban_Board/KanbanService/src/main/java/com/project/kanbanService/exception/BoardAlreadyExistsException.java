package com.project.kanbanService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT , reason="Board Already Exists")
public class BoardAlreadyExistsException extends Exception{
}
