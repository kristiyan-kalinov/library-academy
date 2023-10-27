package com.kodar.academy.Library.model.dto.user;

import com.kodar.academy.Library.model.constants.Constants;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class UserBalanceDTO {

    @Positive(message = Constants.MIN_BALANCE)
    private BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
