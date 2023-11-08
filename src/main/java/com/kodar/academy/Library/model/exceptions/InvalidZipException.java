package com.kodar.academy.Library.model.exceptions;

import com.kodar.academy.Library.model.constants.Constants;

public class InvalidZipException extends RuntimeException{

    public InvalidZipException() {
        super(Constants.INVALID_ZIP);
    }

}
