package com.kodar.academy.Library.service;

import com.kodar.academy.Library.model.entity.BookXMLImportAudit;
import com.kodar.academy.Library.model.eventlistener.BookXMLImportBaseEvent;
import com.kodar.academy.Library.model.mapper.BookMapper;
import com.kodar.academy.Library.repository.BookXMLImportAuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookXMLImportAuditService {

    private final BookXMLImportAuditRepository bookXMLImportAuditRepository;

    private static final Logger logger = LoggerFactory.getLogger(BookXMLImportAuditService.class);

    @Autowired
    public BookXMLImportAuditService(BookXMLImportAuditRepository bookXMLImportAuditRepository) {
        this.bookXMLImportAuditRepository = bookXMLImportAuditRepository;
    }

    public void createBookXMLImportAudit(BookXMLImportBaseEvent bookXMLImportBaseEvent) {
        logger.info("createBookXMLImportAudit called with params: " + bookXMLImportBaseEvent);

        BookXMLImportAudit bookXMLImportAudit = BookMapper.mapToBookXMLImportAudit(bookXMLImportBaseEvent);
        bookXMLImportAudit.setEventDate(LocalDateTime.now());
        bookXMLImportAuditRepository.save(bookXMLImportAudit);
    }

}
