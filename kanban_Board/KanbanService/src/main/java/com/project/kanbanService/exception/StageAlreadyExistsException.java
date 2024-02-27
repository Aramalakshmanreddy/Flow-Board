package com.project.kanbanService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT,reason = "Stage Already Exists")
public class StageAlreadyExistsException extends Exception
{
}
