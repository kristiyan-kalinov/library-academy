package com.kodar.academy.Library.model.exceptions;

import com.kodar.academy.Library.model.constants.Constants;

public class InsufficientBalanceException extends RuntimeException{

    public InsufficientBalanceException(int id) {
        super(String.format(Constants.INSUFFICIENT_BALANCE, id));
    }

}
