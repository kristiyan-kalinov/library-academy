package com.kodar.academy.Library.service;

import com.kodar.academy.Library.model.entity.BookAuditLog;
import com.kodar.academy.Library.model.eventlistener.BookBaseEvent;
import com.kodar.academy.Library.model.mapper.BookMapper;
import com.kodar.academy.Library.repository.BookAuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookAuditLogService {

    private final BookAuditLogRepository bookAuditLogRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookAuditLogService.class);

    @Autowired
    public BookAuditLogService(BookAuditLogRepository bookAuditLogRepository) {
        this.bookAuditLogRepository = bookAuditLogRepository;
    }

    public void createBookAuditLog(BookBaseEvent bookBaseEvent) {
        logger.info("createBookAuditLog called with params: " + bookBaseEvent.toString());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        BookAuditLog bookAuditLog = BookMapper.mapToBookAuditLog(bookBaseEvent);
        bookAuditLog.setTimestamp(LocalDateTime.now());
        bookAuditLog.setPerformedBy(authentication.getName());
        bookAuditLogRepository.save(bookAuditLog);
    }
}
