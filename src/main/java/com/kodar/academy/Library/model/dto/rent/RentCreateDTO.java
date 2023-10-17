package com.kodar.academy.Library.model.dto.rent;

import com.kodar.academy.Library.model.constants.Constants;
import jakarta.validation.constraints.Future;

import java.time.LocalDate;

public class RentCreateDTO {

    private Integer userId;
    @Future(message = Constants.FUTURE_DATE_EXPECTED)
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
