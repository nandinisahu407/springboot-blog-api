package com.example.blog.services;

import com.example.blog.dto.UserDto;
import com.example.blog.entity.Role;
import com.example.blog.entity.User;

import java.util.List;

public interface UserService {
    UserDto registerNewUser(UserDto userDto);
    UserDto createUser(UserDto userDto);
    String updateUser(UserDto userDto,Integer userId);
    UserDto getUserById(Integer userId);
    List<UserDto> getAllUsers();
    String deleteUser(Integer userId);
    String assignRole(Integer userId, Role role);
}
