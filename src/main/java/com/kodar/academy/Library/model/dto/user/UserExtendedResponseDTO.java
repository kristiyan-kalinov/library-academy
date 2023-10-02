package com.kodar.academy.Library.model.dto.user;

import com.kodar.academy.Library.model.entity.Rent;

import java.util.Set;

public class UserExtendedResponseDTO extends UserResponseDTO{

    private Set<Rent> rents;

    public Set<Rent> getRents() {
        return rents;
    }

    public void setRents(Set<Rent> rents) {
        this.rents = rents;
    }

}
