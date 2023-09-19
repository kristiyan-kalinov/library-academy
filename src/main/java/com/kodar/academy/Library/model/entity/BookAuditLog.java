package com.kodar.academy.Library.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "book_audit_log")
public class BookAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "action_performed")
    private String actionPerformed;

    @Column(name = "book_id")
    private int bookId;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "performed_by")
    private String performedBy;

    @Column(name = "old_value")
    private String oldValue;

    @Column(name = "new_value")
    private String newValue;

    public BookAuditLog() {

    }

    public BookAuditLog(String actionPerformed, int bookId, LocalDateTime timestamp,
                        String performedBy, String oldValue, String newValue) {
        this.actionPerformed = actionPerformed;
        this.bookId = bookId;
        this.timestamp = timestamp;
        this.performedBy = performedBy;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActionPerformed() {
        return actionPerformed;
    }

    public void setActionPerformed(String actionPerformed) {
        this.actionPerformed = actionPerformed;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
