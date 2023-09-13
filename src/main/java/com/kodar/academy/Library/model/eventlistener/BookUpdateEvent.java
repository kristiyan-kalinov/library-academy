package com.kodar.academy.Library.model.eventlistener;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

public class BookUpdateEvent extends ApplicationEvent {

    private String actionPerformed;
    private int bookId;
    private LocalDateTime eventTimestamp;
    private String performedBy;
    private String oldValue;
    private String newValue;

    public BookUpdateEvent(Object source, String actionPerformed, int bookId, LocalDateTime eventTimestamp,
                           String performedBy, String oldValue, String newValue) {
        super(source);
        this.actionPerformed = actionPerformed;
        this.bookId = bookId;
        this.eventTimestamp = eventTimestamp;
        this.performedBy = performedBy;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getActionPerformed() {
        return actionPerformed;
    }

    public int getBookId() {
        return bookId;
    }

    public LocalDateTime getEventTimestamp() {
        return eventTimestamp;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }
}
