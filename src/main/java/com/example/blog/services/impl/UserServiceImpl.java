package com.example.blog.services.impl;

import com.example.blog.dto.UserDto;
import com.example.blog.entity.Role;
import com.example.blog.entity.User;
import com.example.blog.exceptions.ResourceNotFoundException;
import com.example.blog.exceptions.UserAlreadyExist;
import com.example.blog.repository.RoleRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDto registerNewUser(UserDto userDto) {
        User user=this.modelMapper.map(userDto,User.class);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        //assign role
        Role role=this.roleRepository.findById(2).get();   //by default assign 'normal' role to user while creating -> 2
        user.getRoles().add(role);
        User saveduser=this.userRepository.save(user);
        return this.modelMapper.map(saveduser,UserDto.class);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if(userRepository.findById(userDto.getId()).isPresent()){
            throw new UserAlreadyExist("User Id: "+userDto.getId()+" Already exists, canot create new user!");
        }
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);
        User user=this.dtoToEntity(userDto);
        User saveduser=this.userRepository.save(user);
        return this.EntityTODto(saveduser);
    }

    @Override
    public String updateUser(UserDto userDto, Integer userId) {
        User user=this.userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","user ID",userId));
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        if (!userDto.getPassword().equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.setAbout(userDto.getAbout());

        this.userRepository.save(user);
        return "Updated user successfully !";
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user=userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","user ID",userId));
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
         User user=this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","user ID",userId));
         this.userRepository.deleteById(userId);
         return "Successfully deleted";
    }

    private User dtoToEntity(UserDto dto){
        User user=this.modelMapper.map(dto,User.class);
        return user;
    }

    private UserDto EntityTODto(User user){
        UserDto dto=this.modelMapper.map(user,UserDto.class);
        return dto;
    }

}
