package com.kodar.academy.Library.model.mapper;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.dto.book.BookCreateDTO;
import com.kodar.academy.Library.model.dto.book.BookResponseDTO;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.entity.BookAuditLog;
import com.kodar.academy.Library.model.eventlistener.BookUpdateEvent;
import com.kodar.academy.Library.repository.AuthorRepository;
import com.kodar.academy.Library.repository.GenreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class BookMapper {

    GenreRepository genreRepository;
    AuthorRepository authorRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookMapper.class);

    @Autowired
    public BookMapper(GenreRepository genreRepository, AuthorRepository authorRepository) {
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
    }

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
        return target;
    }

    public static Book mapToBook(BookCreateDTO source) {
        logger.info("mapToBook called");
        Book target = new Book();
        target.setIsbn(source.getIsbn());
        target.setTitle(source.getTitle());
        target.setYear(source.getYear());
        target.setPublisher(source.getPublisher());
        target.setIsActive(true);
        target.setAvailableQuantity(source.getTotalQuantity());
        target.setTotalQuantity(source.getTotalQuantity());
        return target;
    }

    public static BookAuditLog mapToBookAuditLog(BookUpdateEvent source) {
        logger.info("mapToBookAuditLog(fromBookUpdateEvent) called");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        BookAuditLog target = new BookAuditLog();
        target.setActionPerformed(source.getActionPerformed());
        target.setBookId(source.getBookId());
        target.setTimestamp(LocalDateTime.now());
        target.setPerformedBy(authentication.getName());
        target.setOldValue(source.getOldValue());
        target.setNewValue(source.getNewValue());
        return target;
    }

    public static BookAuditLog mapToBookAuditLog(Book source) {
        logger.info("mapToBookAuditLog(fromBook) called");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        BookAuditLog target = new BookAuditLog();
        target.setActionPerformed(Constants.CREATE_ACTION);
        target.setTimestamp(LocalDateTime.now());
        target.setBookId(source.getId());
        target.setPerformedBy(authentication.getName());
        return target;
    }

}
