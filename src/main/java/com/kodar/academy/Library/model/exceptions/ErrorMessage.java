package com.kodar.academy.Library.model.exceptions;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorMessage {

    private int statusCode;
    private LocalDateTime timestamp;
    private List<String> messages;

    public ErrorMessage(LocalDateTime timestamp, List<String> messages) {
        this.timestamp = timestamp;
        this.messages = messages;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
