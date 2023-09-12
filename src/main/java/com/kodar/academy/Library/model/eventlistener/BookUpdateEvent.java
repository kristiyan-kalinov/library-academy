package com.kodar.academy.Library.model.eventlistener;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

public class BookUpdateEvent extends ApplicationEvent {

    private String actionPerformed;
    private int bookId;
    private LocalDateTime eventTimestamp;
    private String performedBy;
    private String oldTitleValue;
    private String newTitleValue;
    private String oldPublisherValue;
    private String newPublisherValue;

    public BookUpdateEvent(Object source, String actionPerformed, int bookId, LocalDateTime eventTimestamp,
                           String performedBy, String oldTitleValue, String newTitleValue,
                           String oldPublisherValue, String newPublisherValue) {
        super(source);
        this.actionPerformed = actionPerformed;
        this.bookId = bookId;
        this.eventTimestamp = eventTimestamp;
        this.performedBy = performedBy;
        this.oldTitleValue = oldTitleValue;
        this.newTitleValue = newTitleValue;
        this.oldPublisherValue = oldPublisherValue;
        this.newPublisherValue = newPublisherValue;
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

    public String getOldTitleValue() {
        return oldTitleValue;
    }

    public String getNewTitleValue() {
        return newTitleValue;
    }

    public String getOldPublisherValue() {
        return oldPublisherValue;
    }

    public String getNewPublisherValue() {
        return newPublisherValue;
    }
}
