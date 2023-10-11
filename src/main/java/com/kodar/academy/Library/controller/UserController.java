package com.kodar.academy.Library.controller;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.dto.user.UserCPDTO;
import com.kodar.academy.Library.model.dto.user.UserEditDTO;
import com.kodar.academy.Library.model.dto.user.UserExtendedResponseDTO;
import com.kodar.academy.Library.model.dto.user.UserResponseDTO;
import com.kodar.academy.Library.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @userService.checkAuth(#id) == authentication.name")
    public ResponseEntity<UserExtendedResponseDTO> getUserById(@PathVariable("id") int id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @DeleteMapping("/users/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(Constants.SUCCESSFUL_USER_DELETE, HttpStatus.OK);
    }

    @PutMapping("/users/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @userService.checkAuth(#id) == authentication.name")
    public ResponseEntity<?> editUser(@PathVariable("id") int id, @Valid @RequestBody UserEditDTO userEditDTO) {
        return new ResponseEntity<>(userService.editUser(id, userEditDTO), HttpStatus.OK);
    }

    @PutMapping("/users/change-password/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @userService.checkAuth(#id) == authentication.name")
    public ResponseEntity<String> changePassword(@PathVariable("id") int id, @Valid @RequestBody UserCPDTO userCPDTO) {
        userService.changePassword(id, userCPDTO);
        return new ResponseEntity<>(Constants.SUCCESSFUL_PASSWORD_CHANGE, HttpStatus.OK);
    }

}
