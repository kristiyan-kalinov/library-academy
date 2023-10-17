package com.kodar.academy.Library.model.dto.author;

import com.kodar.academy.Library.model.constants.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AuthorDTO {

    @NotBlank(message = Constants.FNAME_REQUIRED)
    @Size(min = 1, max = 64, message = Constants.FNAME_LENGTH)
    @Pattern(regexp = "^[a-zA-Z]+$", message = Constants.FNAME_LETTERS)
    private String firstName;

    @NotBlank(message = Constants.LNAME_REQUIRED)
    @Size(min = 1, max = 64, message = Constants.LNAME_LENGTH)
    @Pattern(regexp = "^[a-zA-Z]+$", message = Constants.LNAME_LETTERS)
    private String lastName;

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

    @Override
    public String toString() {
        return "{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
