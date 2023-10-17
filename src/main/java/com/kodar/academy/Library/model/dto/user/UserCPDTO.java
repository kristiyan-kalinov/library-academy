package com.kodar.academy.Library.model.dto.user;

import com.kodar.academy.Library.model.constants.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserCPDTO {

    @NotBlank(message = Constants.PASSWORD_REQUIRED)
    @Size(min = 8, max = 32, message = Constants.PASSWORD_LENGTH)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
