package com.kodar.academy.Library.model.eventlistener;

import com.kodar.academy.Library.service.BookAuditLogService;
import com.kodar.academy.Library.service.BookXMLImportAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BookEventListener {

    private final BookAuditLogService bookAuditLogService;
    private final BookXMLImportAuditService bookXMLImportAuditService;

    @Autowired
    public BookEventListener(BookAuditLogService bookAuditLogService,
                             BookXMLImportAuditService bookXMLImportAuditService) {
        this.bookAuditLogService = bookAuditLogService;
        this.bookXMLImportAuditService = bookXMLImportAuditService;
    }

    @EventListener
    void handleUpdateTitleEvent(BookUpdateTitleEvent bookUpdateTitleEvent) {
        bookAuditLogService.createBookAuditLog(bookUpdateTitleEvent);
    }

    @EventListener
    void handleUpdatePublisherEvent(BookUpdatePublisherEvent bookUpdatePublisherEvent) {
        bookAuditLogService.createBookAuditLog(bookUpdatePublisherEvent);
    }

    @EventListener
    void handleUpdateStatusEvent(BookUpdateStatusEvent bookUpdateStatusEvent) {
        bookAuditLogService.createBookAuditLog(bookUpdateStatusEvent);
    }

    @EventListener
    void handleUpdateDeactReasonEvent(BookUpdateDeactReasonEvent bookUpdateDeactReasonEvent) {
        bookAuditLogService.createBookAuditLog(bookUpdateDeactReasonEvent);
    }

    @EventListener
    void handleUpdateTotalQuantityEvent(BookUpdateTotalQuantityEvent bookUpdateTotalQuantityEvent) {
        bookAuditLogService.createBookAuditLog(bookUpdateTotalQuantityEvent);
    }

    @EventListener
    void handleUpdateAvailableQuantityEvent(BookUpdateAvailableQuantityEvent bookUpdateAvailableQuantityEvent) {
        bookAuditLogService.createBookAuditLog(bookUpdateAvailableQuantityEvent);
    }

    @EventListener
    void handleCreateEvent(BookCreateEvent bookCreateEvent) {
        bookAuditLogService.createBookAuditLog(bookCreateEvent);
    }

    @EventListener
    void handleSuccessfulImportEvent(BookXMLImportSuccessEvent bookXMLImportSuccessEvent) {
        bookXMLImportAuditService.createBookXMLImportAudit(bookXMLImportSuccessEvent);
    }

    @EventListener
    void handleFailedImportEvent(BookXMLImportFailEvent bookXMLImportFailEvent) {
        bookXMLImportAuditService.createBookXMLImportAudit(bookXMLImportFailEvent);
    }

}
