package com.kodar.academy.Library.model.exceptions;

import com.kodar.academy.Library.model.constants.Constants;

public class InsufficientBookAvailableQuantityException extends RuntimeException{

    public InsufficientBookAvailableQuantityException() {
        super(Constants.INSUFFICIENT_BOOK_AVAILABLE_QUANTITY);
    }

}
