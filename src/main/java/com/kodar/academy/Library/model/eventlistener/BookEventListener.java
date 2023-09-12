package com.kodar.academy.Library.model.eventlistener;

import com.kodar.academy.Library.model.entity.BookAuditLog;
import com.kodar.academy.Library.model.mapper.BookMapper;
import com.kodar.academy.Library.repository.BookAuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BookEventListener {

    private final BookAuditLogRepository bookAuditLogRepository;

    @Autowired
    public BookEventListener(BookAuditLogRepository bookAuditLogRepository) {
        this.bookAuditLogRepository = bookAuditLogRepository;
    }

    @EventListener
    void handleUpdateEvent(BookUpdateEvent bookUpdateEvent) {
        BookAuditLog bookAuditLog = BookMapper.mapToBookAuditLog(bookUpdateEvent);
        bookAuditLogRepository.save(bookAuditLog);
    }

}
