package com.kodar.academy.Library.model.eventlistener;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.entity.Book;

public class BookUpdateTitleEvent extends BookBaseEvent {
    public BookUpdateTitleEvent(String oldValue, Book book) {
        super(Constants.UPDATE_TITLE_ACTION, book.getId(), oldValue, book.getTitle());
    }
}
