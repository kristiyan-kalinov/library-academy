package com.kodar.academy.Library.model.exceptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorMessage {

    private int statusCode;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;
    private final List<String> messages;

    @JsonCreator
    public ErrorMessage(@JsonProperty("timestamp") LocalDateTime timestamp, @JsonProperty("messages") List<String> messages) {
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
