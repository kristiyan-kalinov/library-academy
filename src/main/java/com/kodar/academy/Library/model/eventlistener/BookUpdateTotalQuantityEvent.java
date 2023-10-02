package com.kodar.academy.Library.model.eventlistener;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.entity.Book;

public class BookUpdateTotalQuantityEvent extends BookUpdateEvent{
    public BookUpdateTotalQuantityEvent(String oldValue, Book book) {
        super(Constants.UPDATE_TOTAL_QUANTITY_ACTION, book.getId(), oldValue, String.valueOf(book.getTotalQuantity()));
    }
}
