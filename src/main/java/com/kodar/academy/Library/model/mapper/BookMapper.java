package com.kodar.academy.Library.model.mapper;

import com.kodar.academy.Library.model.dto.book.BookCreateDTO;
import com.kodar.academy.Library.model.dto.book.BookResponseDTO;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.entity.BookAuditLog;
import com.kodar.academy.Library.model.entity.BookXMLImportAudit;
import com.kodar.academy.Library.model.eventlistener.BookBaseEvent;
import com.kodar.academy.Library.model.eventlistener.BookXMLImportBaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public class BookMapper {

    private static final Logger logger = LoggerFactory.getLogger(BookMapper.class);

    public static BookResponseDTO mapToResponse(Book source) {
        logger.info("mapToResponse called");
        BookResponseDTO target = new BookResponseDTO();
        target.setIsbn(source.getIsbn());
        target.setTitle(source.getTitle());
        target.setYear(source.getYear());
        target.setPublisher(source.getPublisher());
        target.setIsActive(source.getIsActive());
        target.setDeactivationReason(source.getDeactivationReason());
        target.setAuthors(source.getAuthors().stream()
                .map(AuthorMapper::mapToResponse)
                .collect(Collectors.toSet()));
        target.setGenres(source.getGenres().stream()
                .map(GenreMapper::mapToResponse)
                .collect(Collectors.toSet()));
        target.setAvailableQuantity(source.getAvailableQuantity());
        target.setTotalQuantity(source.getTotalQuantity());
        return target;
    }

    public static Book mapToBook(BookCreateDTO source) {
        logger.info("mapToBook called");
        Book target = new Book();
        target.setIsbn(source.getIsbn());
        target.setTitle(source.getTitle());
        target.setYear(source.getYear());
        target.setPublisher(source.getPublisher());
        target.setAvailableQuantity(source.getTotalQuantity());
        target.setTotalQuantity(source.getTotalQuantity());
        return target;
    }

    public static BookAuditLog mapToBookAuditLog(BookBaseEvent source) {
        logger.info("mapToBookAuditLog(fromBookUpdateEvent) called");

        BookAuditLog target = new BookAuditLog();
        target.setActionPerformed(source.getActionPerformed());
        target.setBookId(source.getBookId());
        target.setOldValue(source.getOldValue());
        target.setNewValue(source.getNewValue());
        return target;
    }

    public static BookXMLImportAudit mapToBookXMLImportAudit(BookXMLImportBaseEvent source) {
        logger.info("mapToBookXMLImportAudit called");

        BookXMLImportAudit target = new BookXMLImportAudit();
        target.setZipFileName(source.getZipFileName());
        target.setXmlFileName(source.getXmlFileName());
        target.setMessage(source.getMessage());
        return target;
    }

}
