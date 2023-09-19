package com.kodar.academy.Library.model.eventlistener;

import com.kodar.academy.Library.service.BookAuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BookEventListener {

    private final BookAuditLogService bookAuditLogService;

    @Autowired
    public BookEventListener(BookAuditLogService bookAuditLogService) {
        this.bookAuditLogService = bookAuditLogService;
    }

    @EventListener
    void handleUpdateTitleEvent(BookUpdateTitleEvent bookUpdateTitleEvent) {
        bookAuditLogService.createBookAuditLog(bookUpdateTitleEvent);
    }

    @EventListener
    void handleUpdatePublisherEvent(BookUpdatePublisherEvent bookUpdatePublisherEvent) {
        bookAuditLogService.createBookAuditLog(bookUpdatePublisherEvent);
    }

}
