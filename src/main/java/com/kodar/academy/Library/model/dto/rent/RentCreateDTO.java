package com.kodar.academy.Library.model.dto.rent;

import java.time.LocalDate;

public class RentCreateDTO {

    private Integer userId;
    private LocalDate expectedReturnDate;

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

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
                ", expectedReturnDate=" + expectedReturnDate +
                '}';
    }
}
