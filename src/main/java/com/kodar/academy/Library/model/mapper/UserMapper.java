package com.kodar.academy.Library.model.mapper;

import com.kodar.academy.Library.model.dto.user.UserExtendedResponseDTO;
import com.kodar.academy.Library.model.dto.user.UserRegisterDTO;
import com.kodar.academy.Library.model.dto.user.UserResponseDTO;
import com.kodar.academy.Library.model.entity.User;
import com.kodar.academy.Library.model.enums.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public class UserMapper {

    private static final Logger logger = LoggerFactory.getLogger(UserMapper.class);
    public static UserResponseDTO mapToResponse(User source) {
        logger.info("mapToResponse called");
        UserResponseDTO target = new UserResponseDTO();
        target.setUsername(source.getUsername());
        target.setDisplayName(source.getDisplayName());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setDateOfBirth(source.getDateOfBirth());
        target.setHasProlongedRents(source.getHasProlongedRents());
        return target;
    }

    public static UserExtendedResponseDTO mapToExtendedResponse(User source) {
        logger.info("mapToResponse called");
        UserExtendedResponseDTO target = new UserExtendedResponseDTO();
        target.setUsername(source.getUsername());
        target.setDisplayName(source.getDisplayName());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setDateOfBirth(source.getDateOfBirth());
        target.setHasProlongedRents(source.getHasProlongedRents());
        target.setRents(source.getRents().stream().map(RentMapper::mapToResponse).collect(Collectors.toSet()));
        return target;
    }

    public static User mapToUser(UserRegisterDTO source) {
        logger.info("mapToUser called");
        User target = new User();
        target.setUsername(source.getUsername());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setDisplayName(source.getDisplayName());
        target.setRole(Role.USER);
        target.setDateOfBirth(source.getDateOfBirth());
        return target;
    }

}
