package com.kodar.academy.Library.service;

import com.kodar.academy.Library.model.dto.user.UserCPDTO;
import com.kodar.academy.Library.model.dto.user.UserEditDTO;
import com.kodar.academy.Library.model.dto.user.UserRegisterDTO;
import com.kodar.academy.Library.model.dto.user.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO getUserById(int id);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO createUser(UserRegisterDTO userRegisterDTO);
    UserResponseDTO updateUser(int id, UserEditDTO userEditDTO);
    void deleteUser(int id);
    void changePassword(int id, UserCPDTO userCPDTO);

}
