package com.kodar.academy.Library.service;

import com.kodar.academy.Library.model.dto.user.*;
import com.kodar.academy.Library.model.entity.User;
import com.kodar.academy.Library.model.exceptions.UserNotFoundException;
import com.kodar.academy.Library.model.mapper.UserMapper;
import com.kodar.academy.Library.repository.BookAuditLogRepository;
import com.kodar.academy.Library.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BookAuditLogRepository bookAuditLogRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           BookAuditLogRepository bookAuditLogRepository) {
        this.userRepository = userRepository;
        this.bookAuditLogRepository = bookAuditLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserExtendedResponseDTO getUserById(int id) {
        logger.info("getUserById called with params: " + id);
        User user = userRepository.findById(id).orElse(null);
        if(user != null) {
            return UserMapper.mapToExtendedResponse(user);
        }
        else throw new UserNotFoundException(id);
    }

    public List<UserResponseDTO> getAllUsers() {
        logger.info("getAllUsers called");
        List<UserResponseDTO> users = new ArrayList<>();
        userRepository.findAll().stream()
                .map(UserMapper::mapToResponse)
                .forEach(users::add);

        return users;

    }

    @Transactional
    public UserResponseDTO createUser(UserRegisterDTO userRegisterDTO) {
        logger.info("createUser called with params: " + userRegisterDTO.toString());
        User user = UserMapper.mapToUser(userRegisterDTO);
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        userRepository.save(user);

        return UserMapper.mapToResponse(user);

    }

    @Transactional
    public UserResponseDTO editUser(int id, UserEditDTO userEditDTO) {
        logger.info("editUser called for user with id: " + id + " and params: " + userEditDTO.toString());
        User user = userRepository.findById(id).orElse(null);
        if(user == null) {
            throw new UserNotFoundException(id);
        }
        user.setUsername(userEditDTO.getUsername());
        user.setFirstName(userEditDTO.getFirstName());
        user.setLastName(userEditDTO.getLastName());
        user.setDisplayName(userEditDTO.getDisplayName());
        userRepository.save(user);
        return UserMapper.mapToResponse(user);

    }

    @Transactional
    public void deleteUser(int id) {
        logger.info("deleteUser called with params: " + id);
        User user = userRepository.findById(id).orElse(null);
        if(user != null) {
            bookAuditLogRepository.setUserIdToNull(user.getUsername());
            userRepository.deleteById(id);
        }
        else throw new UserNotFoundException(id);
    }

    public void changePassword(int id, UserCPDTO userCPDTO) {
        logger.info("changePassword called for user with id: " + id);
        User user = userRepository.findById(id).orElse(null);
        if(user == null) {
            throw new UserNotFoundException(id);
        }
        user.setPassword(passwordEncoder.encode(userCPDTO.getPassword()));
        userRepository.save(user);
    }

    public String checkAuth(int id) {
        User user = userRepository.findById(id).orElse(null);
        return user == null ? null : user.getUsername();
    }
}
