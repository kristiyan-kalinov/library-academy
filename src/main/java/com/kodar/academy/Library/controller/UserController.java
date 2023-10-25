package com.kodar.academy.Library.controller;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.dto.user.UserBalanceDTO;
import com.kodar.academy.Library.model.dto.user.UserCPDTO;
import com.kodar.academy.Library.model.dto.user.UserEditDTO;
import com.kodar.academy.Library.model.dto.user.UserExtendedResponseDTO;
import com.kodar.academy.Library.model.dto.user.UserResponseDTO;
import com.kodar.academy.Library.model.dto.user.UserSubscriptionDTO;
import com.kodar.academy.Library.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

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

    @PutMapping("/users/subscribe/{id}")
    @PreAuthorize("@userService.checkAuth(#id) == authentication.name")
    public ResponseEntity<String> subscribe(@PathVariable("id") int id, @Valid @RequestBody UserSubscriptionDTO userSubscriptionDTO) {
        userService.subscribe(id, userSubscriptionDTO);
        return new ResponseEntity<>(Constants.SUCCESSFUL_SUBSCRIPTION, HttpStatus.OK);
    }

    @PutMapping("/users/unsubscribe/{id}")
    @PreAuthorize("@userService.checkAuth(#id) == authentication.name")
    public ResponseEntity<String> unsubscribe(@PathVariable("id") int id) {
        userService.unsubscribe(id);
        return new ResponseEntity<>(Constants.SUCCESSFUL_UNSUBSCRIBE, HttpStatus.OK);
    }

    @PutMapping("/users/add-balance/{id}")
    @PreAuthorize("@userService.checkAuth(#id) == authentication.name")
    public ResponseEntity<String> addBalance(@PathVariable("id") int id, @Valid @RequestBody UserBalanceDTO userBalanceDTO) {
        userService.addBalance(id, userBalanceDTO);
        return new ResponseEntity<>(String.format(Constants.ADD_BALANCE, id, userBalanceDTO.getBalance()), HttpStatus.OK);
    }

}
