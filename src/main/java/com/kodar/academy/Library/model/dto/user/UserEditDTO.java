package com.kodar.academy.Library.model.dto.user;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.validation.MatchingOldUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserEditDTO {

    @MatchingOldUsername
    @NotBlank(message = Constants.USERNAME_REQUIRED)
    @Size(min = 3, max = 32, message = Constants.USERNAME_LENGTH)
    private String username;
    @NotBlank(message = Constants.FNAME_REQUIRED)
    @Size(min = 1, max = 64, message = Constants.FNAME_LENGTH)
    @Pattern(regexp = "^[a-zA-Z]+$", message = Constants.FNAME_LETTERS)
    private String firstName;
    @NotBlank(message = Constants.LNAME_REQUIRED)
    @Size(min = 1, max = 64, message = Constants.LNAME_LENGTH)
    @Pattern(regexp = "^[a-zA-Z]+$", message = Constants.LNAME_LETTERS)
    private String lastName;
    @NotBlank(message = Constants.DISPLAY_NAME_REQUIRED)
    @Size(min = 3, max = 32, message = Constants.DISPLAY_NAME_LENGTH)
    private String displayName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
