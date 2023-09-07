package com.kodar.academy.Library.service.impl;

import com.kodar.academy.Library.model.dto.user.UserCPDTO;
import com.kodar.academy.Library.model.dto.user.UserEditDTO;
import com.kodar.academy.Library.model.dto.user.UserRegisterDTO;
import com.kodar.academy.Library.model.dto.user.UserResponseDTO;
import com.kodar.academy.Library.model.entity.User;
import com.kodar.academy.Library.model.mapper.UserMapper;
import com.kodar.academy.Library.repository.UserRepository;
import com.kodar.academy.Library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO getUserById(int id) {
        Optional<User> userData = userRepository.findById(id);
        if(userData.isPresent()) {
            User user = userData.get();
            return UserMapper.mapToResponse(user);
        }
        return null;
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        List<UserResponseDTO> users = new ArrayList<>();
        userRepository.findAll().stream()
                .map(UserMapper::mapToResponse)
                .forEach(users::add);

        return users;

    }

    @Override
    public UserResponseDTO createUser(UserRegisterDTO userRegisterDTO) {
        User user = UserMapper.mapToUser(userRegisterDTO);
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        userRepository.save(user);

        return UserMapper.mapToResponse(user);

    }

    @Override
    public UserResponseDTO updateUser(int id, UserEditDTO userEditDTO) {

        User oldUser = userRepository.findById(id).orElseThrow();
        oldUser.setUsername(userEditDTO.getUsername());
        oldUser.setFirstName(userEditDTO.getFirstName());
        oldUser.setLastName(userEditDTO.getLastName());
        oldUser.setDisplayName(userEditDTO.getDisplayName());
        userRepository.save(oldUser);
        return UserMapper.mapToResponse(oldUser);

    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public void changePassword(int id, UserCPDTO userCPDTO) {
        User oldUser = userRepository.findById(id).orElseThrow();
        oldUser.setPassword(passwordEncoder.encode(userCPDTO.getPassword()));
        userRepository.save(oldUser);
    }
}
