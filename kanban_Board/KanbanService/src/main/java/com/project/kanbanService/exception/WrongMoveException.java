package com.project.kanbanService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE , reason="Work in-progress task exceeded the maximum limit")
public class WrongMoveException extends Exception{
}
