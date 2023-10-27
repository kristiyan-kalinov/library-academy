package com.kodar.academy.Library.model.exceptions;

import com.kodar.academy.Library.model.constants.Constants;

public class BookNotFoundException extends RuntimeException{

    public BookNotFoundException(int id) {
        super(String.format(Constants.BOOK_NOT_FOUND, id));
    }

}
