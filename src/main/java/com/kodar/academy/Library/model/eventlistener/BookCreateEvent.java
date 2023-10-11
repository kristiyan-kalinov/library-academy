package com.kodar.academy.Library.model.eventlistener;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.entity.Book;

public class BookCreateEvent extends BookBaseEvent{
    public BookCreateEvent(Book book) {
        super(Constants.CREATE_ACTION, book.getId(), null, null);
    }
}
