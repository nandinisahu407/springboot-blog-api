package com.example.blog.controllers;

import com.example.blog.dto.UserDto;
import com.example.blog.entity.User;
import com.example.blog.exceptions.UserAlreadyExist;
import com.example.blog.exceptions.UserException;
import com.example.blog.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    //create
    @PostMapping("/")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto){
        try {
            UserDto createdUser=this.userService.createUser(userDto);
            return  new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }catch(UserAlreadyExist ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    //update
    @PutMapping("/{userId}")
    public String updateDetails(@Valid @RequestBody UserDto userDto, @PathVariable Integer userId){
        if(userDto==null){
            return "Body cannot be null!";
        }
        return this.userService.updateUser(userDto,userId);
    }

    //get user by id
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserDetailById(@PathVariable Integer userId ){
        UserDto user=this.userService.getUserById(userId);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/allUsers")
    public List<UserDto> getAlluserDetails(){
        return this.userService.getAllUsers();
    }

    //delete
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Integer userId){
        return this.userService.deleteUser(userId);
    }


}
