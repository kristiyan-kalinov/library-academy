package com.kodar.academy.Library.model.exceptions;

import com.kodar.academy.Library.model.constants.Constants;

public class InvalidFileException extends RuntimeException{

    public InvalidFileException() {
        super(Constants.INVALID_FILE);
    }

}
