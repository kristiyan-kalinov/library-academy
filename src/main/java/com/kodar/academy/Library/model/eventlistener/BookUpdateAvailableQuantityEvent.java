package com.kodar.academy.Library.model.eventlistener;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.entity.Book;

public class BookUpdateAvailableQuantityEvent extends BookUpdateEvent{
    public BookUpdateAvailableQuantityEvent(String oldValue, Book book) {
        super(Constants.UPDATE_AVAILABLE_QUANTITY_ACTION, book.getId(), oldValue, String.valueOf(book.getAvailableQuantity()));
    }
}
