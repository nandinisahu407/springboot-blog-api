package com.example.blog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(value ={UserNotFoundException.class} )
    public ResponseEntity<UserException> handleUserNotFoundException(UserNotFoundException ex){
        UserException userException=new UserException(
                ex.getMessage(),ex.getCause(), HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(userException,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {UserAlreadyExist.class})
    public ResponseEntity<UserException> handleUserAlreadyExistException(UserAlreadyExist ex){
        UserException userException=new UserException(
                ex.getMessage(),ex.getCause(),HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(userException,HttpStatus.BAD_REQUEST);
    }
}
