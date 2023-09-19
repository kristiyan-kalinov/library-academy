package com.kodar.academy.Library.service.impl;

import com.kodar.academy.Library.model.entity.BookAuditLog;
import com.kodar.academy.Library.model.eventlistener.BookUpdateEvent;
import com.kodar.academy.Library.model.mapper.BookMapper;
import com.kodar.academy.Library.repository.BookAuditLogRepository;
import com.kodar.academy.Library.service.BookAuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookAuditLogServiceImpl implements BookAuditLogService {

    private final BookAuditLogRepository bookAuditLogRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookAuditLogServiceImpl.class);

    @Autowired
    public BookAuditLogServiceImpl(BookAuditLogRepository bookAuditLogRepository) {
        this.bookAuditLogRepository = bookAuditLogRepository;
    }

    @Override
    public void createBookAuditLog(BookUpdateEvent bookUpdateEvent) {
        logger.info("createBookAuditLog called with params: " + bookUpdateEvent.toString());
        BookAuditLog bookAuditLog = BookMapper.mapToBookAuditLog(bookUpdateEvent);
        bookAuditLogRepository.save(bookAuditLog);
    }
}
