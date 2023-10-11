package com.kodar.academy.Library.model.exceptions;

import com.kodar.academy.Library.model.constants.Constants;

public class InvalidDeactReasonException extends RuntimeException{

    public InvalidDeactReasonException() {
        super(Constants.INVALID_DEACT_REASON);
    }

}
