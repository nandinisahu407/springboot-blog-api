package com.example.blog.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class UserAlreadyExist extends RuntimeException{
    private  String message;
    public UserAlreadyExist(String message) {
        super(message);
        this.message = message;
    }

}
