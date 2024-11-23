package com.example.blog.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private int id;
    @NotEmpty
    @Size(min=4,message = "Username must be of min 4 size")
    private  String userName;
    @NotBlank(message = "Email is required!")
    @Email(message = "Please provide valid email")
    private String email;
    @NotEmpty(message = "Password is required!")
    @Size(min = 6,message = "Password should be of min length 6")
    private String password;
    private String about;
}
