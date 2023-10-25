package com.kodar.academy.Library.model.dto.user;

import java.time.LocalDate;

public class UserResponseDTO {

    private String username;

    private String firstName;

    private String lastName;

    private String displayName;

    private LocalDate dateOfBirth;

    private boolean hasProlongedRents;

    private String subscriptionType;

    private String balance;

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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean getHasProlongedRents() {
        return hasProlongedRents;
    }

    public void setHasProlongedRents(boolean hasProlongedRents) {
        this.hasProlongedRents = hasProlongedRents;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
