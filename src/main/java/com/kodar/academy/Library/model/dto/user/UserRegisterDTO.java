package com.kodar.academy.Library.model.dto.user;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.validation.UniqueUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UserRegisterDTO {

    @UniqueUsername
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
    @NotBlank(message = Constants.PASSWORD_REQUIRED)
    @Size(min = 8, max = 32, message = Constants.PASSWORD_LENGTH)
    private String password;
    @Past(message = Constants.PAST_DATE_EXPECTED)
    private LocalDate dateOfBirth;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
