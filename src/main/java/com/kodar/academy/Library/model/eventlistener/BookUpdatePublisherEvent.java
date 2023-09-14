package com.kodar.academy.Library.model.eventlistener;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.entity.Book;

public class BookUpdatePublisherEvent extends BookUpdateEvent{
    public BookUpdatePublisherEvent(String oldValue, Book book) {
        super(Constants.UPDATE_PUBLISHER_ACTION, book.getId(), oldValue, book.getPublisher());
    }
}
