package com.kodar.academy.Library.model.exceptions;

import com.kodar.academy.Library.model.constants.Constants;

public class UserNotEligibleToRentException extends RuntimeException{

    public UserNotEligibleToRentException(String rentingUser, String rentForUser) {
        super(String.format(Constants.USER_NOT_ELIGIBLE_TO_RENT, rentingUser, rentForUser));
    }

}
