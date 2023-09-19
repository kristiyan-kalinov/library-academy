package com.kodar.academy.Library.model.eventlistener;

public class BookUpdateEvent {

    private String actionPerformed;
    private int bookId;
    private String oldValue;
    private String newValue;

    public BookUpdateEvent(String actionPerformed, int bookId,
                           String oldValue, String newValue) {
        this.actionPerformed = actionPerformed;
        this.bookId = bookId;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getActionPerformed() {
        return actionPerformed;
    }

    public int getBookId() {
        return bookId;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    @Override
    public String toString() {
        return "{" +
                "actionPerformed='" + actionPerformed + '\'' +
                ", bookId=" + bookId +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                '}';
    }
}
