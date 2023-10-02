package com.kodar.academy.Library.service;

import com.kodar.academy.Library.model.dto.user.*;
import com.kodar.academy.Library.model.entity.User;
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
import java.util.Optional;

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
        Optional<User> userData = userRepository.findById(id);
        if(userData.isPresent()) {
            User user = userData.get();
            return UserMapper.mapToExtendedResponse(user);
        }
        return null;
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
        User oldUser = userRepository.findById(id).orElseThrow();
        oldUser.setUsername(userEditDTO.getUsername());
        oldUser.setFirstName(userEditDTO.getFirstName());
        oldUser.setLastName(userEditDTO.getLastName());
        oldUser.setDisplayName(userEditDTO.getDisplayName());
        userRepository.save(oldUser);
        return UserMapper.mapToResponse(oldUser);

    }

    @Transactional
    public void deleteUser(int id) {
        logger.info("deleteUser called with params: " + id);
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            bookAuditLogRepository.setUserIdToNull(user.get().getUsername());
        }
        userRepository.deleteById(id);
    }

    public void changePassword(int id, UserCPDTO userCPDTO) {
        logger.info("changePassword called for user with id: " + id);
        User oldUser = userRepository.findById(id).orElseThrow();
        oldUser.setPassword(passwordEncoder.encode(userCPDTO.getPassword()));
        userRepository.save(oldUser);
    }

    public String checkAuth(int id) {
        User user = userRepository.findById(id).orElseThrow();
        return user.getUsername();
    }
}
