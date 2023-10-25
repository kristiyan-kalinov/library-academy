package com.kodar.academy.Library.model.dto.rent;

public class RentCreateDTO {

    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "RentCreateDTO{" +
                "userId=" + userId +
                '}';
    }
}
