package com.example.blog.services.impl;

import com.example.blog.dto.UserDto;
import com.example.blog.entity.User;
import com.example.blog.repository.UserRepository;
import com.example.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user=this.dtoToEntity(userDto);
        User saveduser=this.userRepository.save(user);
        return this.EntityTODto(saveduser);
    }

    @Override
    public String updateUser(UserDto userDto, Integer userId) {
        if(userRepository.findById(userId).isEmpty()){
            return "Could not update,User with userId:"+userId+" does not exist!";
        }
        User user=this.userRepository.findById(userId).orElseThrow(()->new RuntimeException("not found"));
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());

        this.userRepository.save(user);
        return "Updated user successfully !";
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user=userRepository.findById(userId).orElseThrow(()-> new RuntimeException("user not found!"));
        return this.EntityTODto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users=this.userRepository.findAll();
        return users.stream()
                .map(this::EntityTODto)
                .collect(Collectors.toList());
    }

    @Override
    public String deleteUser(Integer userId) {
         User user=this.userRepository.findById(userId).orElseThrow(()->new RuntimeException("Cannot delete,No such user exists!"));
         this.userRepository.deleteById(userId);
         return "Successfully deleted";
    }

    private User dtoToEntity(UserDto dto){
        User user=new User();
        user.setId(dto.getId());
        user.setUserName(dto.getUserName());
        user.setAbout(dto.getAbout());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

    private UserDto EntityTODto(User user){
        UserDto dto=new UserDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setAbout(user.getAbout());
        return dto;
    }

}
