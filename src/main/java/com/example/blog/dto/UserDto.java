package com.example.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private int id;
    private  String userName;
    @NotBlank(message = "Email is required!")
    @Email(message = "Please provide valid email")
    private String email;
    @NotBlank(message = "Password is required!")
    @Size(min = 6,message = "Password should be of min length 6")
    private String password;
    private String about;
}
