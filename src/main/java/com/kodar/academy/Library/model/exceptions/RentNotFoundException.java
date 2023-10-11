package com.kodar.academy.Library.model.exceptions;

import com.kodar.academy.Library.model.constants.Constants;

public class RentNotFoundException extends RuntimeException{

    public RentNotFoundException(int id) {
        super(String.format(Constants.RENT_NOT_FOUND, id));
    }

}
