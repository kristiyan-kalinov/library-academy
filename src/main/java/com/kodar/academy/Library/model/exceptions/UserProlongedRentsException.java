package com.kodar.academy.Library.model.exceptions;

import com.kodar.academy.Library.model.constants.Constants;

public class UserProlongedRentsException extends RuntimeException{

    public UserProlongedRentsException(String username) {
        super(String.format(Constants.USER_PROLONGED_RENTS, username));
    }

}
