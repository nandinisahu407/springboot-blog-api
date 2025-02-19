package com.example.blog.config;

import com.example.blog.constants.EntityType;
import com.example.blog.dto.UserDto;
import com.example.blog.entity.User;
import com.example.blog.exceptions.ResourceNotFoundException;
import com.example.blog.model.JwtRequest;
import com.example.blog.model.JwtResponse;
import com.example.blog.repository.UserRepository;
import com.example.blog.security.JwtHelper;
import com.example.blog.services.LogEntryService;
import com.example.blog.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LogEntryService logEntryService;

    @Autowired
    private JwtHelper helper;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        logger.info("Reached in controller => ");

        User user=this.userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new ResourceNotFoundException("User","email: "+request.getEmail(),0));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            JwtResponse response=new JwtResponse();
            response.setUsername("Invalid credentials, the password you have entered is incorrect !!");
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);
        JwtResponse response=new JwtResponse();
        response.setJwtToken(token);
        response.setUsername(request.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){
        UserDto registeredUser=this.userService.registerNewUser(userDto);

        logEntryService.logAction(
                userDto.getUserName(),
                "CREATED",
                EntityType.USER,
                userDto.getUserName(),
                "User Created:"+userDto.getUserName()
        );
        return new ResponseEntity<>(registeredUser,HttpStatus.CREATED);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }
}
