package com.kodar.academy.Library.model.exceptions;

import com.kodar.academy.Library.model.constants.Constants;

public class DeleteActiveBookException extends RuntimeException{

    public DeleteActiveBookException() {
        super(Constants.DELETE_ACTIVE_BOOK_MSG);
    }

}
