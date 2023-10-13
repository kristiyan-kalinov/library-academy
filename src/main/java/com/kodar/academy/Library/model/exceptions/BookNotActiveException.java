package com.kodar.academy.Library.model.exceptions;

import com.kodar.academy.Library.model.constants.Constants;

public class BookNotActiveException extends RuntimeException{

    public BookNotActiveException(int id) {
        super(String.format(Constants.BOOK_NOT_ACTIVE, id));
    }

}
