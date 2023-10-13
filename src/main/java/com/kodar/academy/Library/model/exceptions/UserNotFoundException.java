package com.kodar.academy.Library.model.exceptions;

import com.kodar.academy.Library.model.constants.Constants;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(int id) {
        super(String.format(Constants.USER_NOT_FOUND, id));
    }

}
