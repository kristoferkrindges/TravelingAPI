package com.kristofer.traveling.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.kristofer.traveling.services.exceptions.ObjectAlreadyExistsException;
import com.kristofer.traveling.services.exceptions.ObjectNotFoundException;
import com.kristofer.traveling.services.exceptions.ObjectNotNullException;
import com.kristofer.traveling.services.exceptions.ObjectNotPermission;
import com.kristofer.traveling.services.exceptions.PasswordsNotSame;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> 
    objectNotFound(ObjectNotFoundException e, HttpServletRequest request){
    
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(System.currentTimeMillis(), 
        status.value(),"Not found", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ResponseEntity<StandardError> 
    objectAlreadyExists(ObjectAlreadyExistsException e, HttpServletRequest request){
    
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(System.currentTimeMillis(), 
        status.value(),"Already exists: ", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ObjectNotNullException.class)
    public ResponseEntity<StandardError> 
    objectNotNullException(ObjectNotNullException e, HttpServletRequest request){
    
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(System.currentTimeMillis(), 
        status.value(),"Not null: ", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<StandardError> 
    usernameNotFoundException(UsernameNotFoundException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(System.currentTimeMillis(), 
        status.value(),"Token invalid: ", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
    @ExceptionHandler(PasswordsNotSame.class)
    public ResponseEntity<StandardError> 
    passwordNotSame(PasswordsNotSame e, HttpServletRequest request){
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(System.currentTimeMillis(), 
        status.value(),"Passwords: ", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ObjectNotPermission.class)
    public ResponseEntity<StandardError> 
    objectNotPermission(ObjectNotPermission e, HttpServletRequest request){
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError err = new StandardError(System.currentTimeMillis(), 
        status.value(),"Forbidden: ", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}
