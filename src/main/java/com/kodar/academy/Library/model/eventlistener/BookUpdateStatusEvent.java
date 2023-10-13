package com.kodar.academy.Library.model.eventlistener;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.entity.Book;

public class BookUpdateStatusEvent extends BookBaseEvent {
    public BookUpdateStatusEvent(String oldValue, Book book) {
        super(Constants.UPDATE_STATUS_ACTION, book.getId(), oldValue, String.valueOf(book.getIsActive()));
    }
}
