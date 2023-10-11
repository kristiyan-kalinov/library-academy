package com.kodar.academy.Library.model.exceptions;

import com.kodar.academy.Library.model.constants.Constants;

public class RentCapException extends RuntimeException{

    public RentCapException(String username) {
        super(String.format(Constants.RENT_CAP_MSG, username));
    }

}
