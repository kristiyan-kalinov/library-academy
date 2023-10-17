package com.kodar.academy.Library.model.dto.user;

import com.kodar.academy.Library.model.dto.rent.RentResponseDTO;
import com.kodar.academy.Library.model.entity.Rent;

import java.util.Set;

public class UserExtendedResponseDTO extends UserResponseDTO{

    private Set<RentResponseDTO> rents;

    public Set<RentResponseDTO> getRents() {
        return rents;
    }

    public void setRents(Set<RentResponseDTO> rents) {
        this.rents = rents;
    }

}
