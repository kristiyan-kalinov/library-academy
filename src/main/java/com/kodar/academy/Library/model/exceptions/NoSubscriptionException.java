package com.kodar.academy.Library.model.exceptions;

import com.kodar.academy.Library.model.constants.Constants;

public class NoSubscriptionException extends RuntimeException{

    public NoSubscriptionException(int id) {
        super(String.format(Constants.NO_SUBSCRIPTION, id));
    };

}
