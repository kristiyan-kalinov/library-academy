package com.kodar.academy.Library.model.exceptions;

import com.kodar.academy.Library.model.constants.Constants;

public class InsufficientBookTotalQuantityException extends RuntimeException{

    public InsufficientBookTotalQuantityException() {
        super(Constants.INSUFFICIENT_BOOK_TOTAL_QUANTITY);
    }

}
