package com.kodar.academy.Library.model.mapper;

import com.kodar.academy.Library.model.dto.user.UserRegisterDTO;
import com.kodar.academy.Library.model.dto.user.UserResponseDTO;
import com.kodar.academy.Library.model.entity.User;
import com.kodar.academy.Library.model.enums.Role;

public class UserMapper {

    public static UserResponseDTO mapToResponse(User source) {
        UserResponseDTO target = new UserResponseDTO();
        target.setUsername(source.getUsername());
        target.setDisplayName(source.getDisplayName());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setDateOfBirth(source.getDateOfBirth());
        return target;
    }

    public static User mapToUser(UserRegisterDTO source) {
        User target = new User();
        target.setUsername(source.getUsername());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setDisplayName(source.getDisplayName());
        target.setPassword(source.getPassword());
        target.setRole(Role.USER);
        target.setDateOfBirth(source.getDateOfBirth());
        return target;
    }

}
