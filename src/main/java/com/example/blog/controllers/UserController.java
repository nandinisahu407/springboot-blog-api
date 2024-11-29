package com.example.blog.controllers;

import com.example.blog.config.SecurityUtils;
import com.example.blog.dto.RoleDto;
import com.example.blog.dto.UserDto;
import com.example.blog.entity.Role;
import com.example.blog.entity.User;
import com.example.blog.exceptions.UserAlreadyExist;
import com.example.blog.services.RoleService;
import com.example.blog.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private SecurityUtils securityUtils;

    //create
    @PostMapping("/")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto){
        try {
            UserDto createdUser=this.userService.registerNewUser(userDto);
            return  new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }catch(UserAlreadyExist ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    //update-> (role: normal user can update only their own profiles)
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateDetails(@Valid @RequestBody UserDto userDto, @PathVariable Integer userId){
        if(userDto==null){
            return new ResponseEntity<>("Body cannot be null!",HttpStatus.BAD_REQUEST);
        }

        //normal user can update only their own profiles
        User loggedInUser = securityUtils.getLoggedInUserDetails();
        boolean isNormalUser = loggedInUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_NORMAL"));

        if (isNormalUser && (loggedInUser.getId()!=userId)) {
            return new ResponseEntity<>("Normal users can only update their own profile.", HttpStatus.FORBIDDEN);
        }
        //admin,super-admin,normal user(owner)
        String response= this.userService.updateUser(userDto,userId);
        return new ResponseEntity<>(response,HttpStatus.OK);
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Integer userId){
        return this.userService.deleteUser(userId);
    }

    //---------- ROle Assignment-------------
    //Super Admin can assign-> Admin,Viewer,Normal
    //Admin-> Viewer,Normal

    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @PostMapping("/{userId}/assignRole")
    public ResponseEntity<String> assignRoleToUser(@PathVariable Integer userId, @RequestBody RoleDto roleDto) {
        Role role = this.roleService.getRoleByName(roleDto.getName());

        // Check if user is an ADMIN or SUPER_ADMIN
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        boolean isSuperAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_SUPER_ADMIN"));

        // Admins can only assign "Normal" or "Viewer" roles
        if (isAdmin) {
            if (roleDto.getName().equals("ROLE_NORMAL") || roleDto.getName().equals("ROLE_VIEWER")) {
                String response = this.userService.assignRole(userId, role);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin can only assign Normal or Viewer roles.");
            }
        } else if (isSuperAdmin) {
            // Super Admin can assign any role
            String response = this.userService.assignRole(userId, role);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have the required permissions.");
        }
    }


}
