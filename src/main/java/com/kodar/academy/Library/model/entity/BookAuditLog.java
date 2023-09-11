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

    @Column(name = "old_title_value")
    private String oldTitleValue;

    @Column(name = "new_title_value")
    private String newTitleValue;

    @Column(name = "old_publisher_value")
    private String oldPublisherValue;

    @Column(name = "new_publisher_value")
    private String newPublisherValue;

    public BookAuditLog() {

    }

    public BookAuditLog(String actionPerformed, int bookId, LocalDateTime timestamp,
                        String performedBy, String oldTitleValue, String newTitleValue,
                        String oldPublisherValue, String newPublisherValue) {
        this.actionPerformed = actionPerformed;
        this.bookId = bookId;
        this.timestamp = timestamp;
        this.performedBy = performedBy;
        this.oldTitleValue = oldTitleValue;
        this.newTitleValue = newTitleValue;
        this.oldPublisherValue = oldPublisherValue;
        this.newPublisherValue = newPublisherValue;
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

    public String getOldTitleValue() {
        return oldTitleValue;
    }

    public void setOldTitleValue(String oldTitleValue) {
        this.oldTitleValue = oldTitleValue;
    }

    public String getNewTitleValue() {
        return newTitleValue;
    }

    public void setNewTitleValue(String newTitleValue) {
        this.newTitleValue = newTitleValue;
    }

    public String getOldPublisherValue() {
        return oldPublisherValue;
    }

    public void setOldPublisherValue(String oldPublisherValue) {
        this.oldPublisherValue = oldPublisherValue;
    }

    public String getNewPublisherValue() {
        return newPublisherValue;
    }

    public void setNewPublisherValue(String newPublisherValue) {
        this.newPublisherValue = newPublisherValue;
    }
}
