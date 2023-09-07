package com.kodar.academy.Library.controller;

import com.kodar.academy.Library.model.dto.user.UserCPDTO;
import com.kodar.academy.Library.model.dto.user.UserEditDTO;
import com.kodar.academy.Library.model.dto.user.UserResponseDTO;
import com.kodar.academy.Library.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();

        if(users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") int id) {
        UserResponseDTO userResponseDTO = userService.getUserById(id);
        if(userResponseDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") int id) {
        if(userService.getUserById(id) == null) {
            return new ResponseEntity<>("User with that id doesn't exist", HttpStatus.NOT_FOUND);
        }
        userService.deleteUser(id);
        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }

    @PutMapping("/users/edit/{id}")
    public ResponseEntity<?> editUser(@PathVariable("id") int id, @Valid @RequestBody UserEditDTO userEditDTO) {
        if(userService.getUserById(id) == null) {
            return new ResponseEntity<>("User with that id doesn't exist", HttpStatus.NOT_FOUND);
        }
        UserResponseDTO userResponseDTO = userService.updateUser(id, userEditDTO);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/users/change-password/{id}")
    public ResponseEntity<String> changePassword(@PathVariable("id") int id, @RequestBody UserCPDTO userCPDTO) {
        if(userService.getUserById(id) == null) {
            return new ResponseEntity<>("User with that id doesn't exist", HttpStatus.NOT_FOUND);
        }
        userService.changePassword(id, userCPDTO);
        return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
    }

}
