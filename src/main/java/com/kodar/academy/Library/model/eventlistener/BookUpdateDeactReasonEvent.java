package com.kodar.academy.Library.model.eventlistener;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.entity.Book;

public class BookUpdateDeactReasonEvent extends BookUpdateEvent{
    public BookUpdateDeactReasonEvent(String oldValue, Book book) {
        super(Constants.UPDATE_REASON_ACTION, book.getId(), oldValue, book.getDeactivationReason());
    }
}
