package com.kodar.academy.Library.model.exceptions;

import com.kodar.academy.Library.model.constants.Constants;

public class DuplicateRentException extends RuntimeException{

    public DuplicateRentException() {
        super(Constants.DUPLICATE_RENT);
    }

}
