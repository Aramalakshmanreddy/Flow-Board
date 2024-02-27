package com.project.authentication.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED,reason = "Invalid Credentials Given")
public class InvalidCredentialsException extends Exception
{
}
