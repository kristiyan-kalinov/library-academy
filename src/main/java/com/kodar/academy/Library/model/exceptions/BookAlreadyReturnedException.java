package com.kodar.academy.Library.model.exceptions;

import com.kodar.academy.Library.model.constants.Constants;

public class BookAlreadyReturnedException extends RuntimeException{

    public BookAlreadyReturnedException() {
        super(Constants.BOOK_ALREADY_RETURNED);
    }

}
