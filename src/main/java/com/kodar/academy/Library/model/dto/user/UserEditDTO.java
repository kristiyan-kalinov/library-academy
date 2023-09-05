package com.kodar.academy.Library.model.dto.user;

import com.kodar.academy.Library.model.validation.MatchingOldUsername;
import com.kodar.academy.Library.model.validation.UniqueUsername;

public class UserEditDTO {

    @MatchingOldUsername
    private String username;
    private String firstName;
    private String lastName;
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
}
