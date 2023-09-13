package com.kodar.academy.Library.model.mapper;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.dto.book.BookCreateDTO;
import com.kodar.academy.Library.model.dto.book.BookResponseDTO;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.entity.BookAuditLog;
import com.kodar.academy.Library.model.eventlistener.BookUpdateEvent;
import com.kodar.academy.Library.repository.AuthorRepository;
import com.kodar.academy.Library.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    GenreRepository genreRepository;
    AuthorRepository authorRepository;

    @Autowired
    public BookMapper(GenreRepository genreRepository, AuthorRepository authorRepository) {
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
    }

    public static BookResponseDTO mapToResponse(Book source) {
        BookResponseDTO target = new BookResponseDTO();
        target.setIsbn(source.getIsbn());
        target.setTitle(source.getTitle());
        target.setYear(source.getYear());
        target.setPublisher(source.getPublisher());
        target.setAuthors(source.getAuthors().stream()
                .map(AuthorMapper::mapToResponse)
                .collect(Collectors.toSet()));
        target.setGenres(source.getGenres().stream()
                .map(GenreMapper::mapToResponse)
                .collect(Collectors.toSet()));
        return target;
    }

    public static Book mapToBook(BookCreateDTO source) {
        Book target = new Book();
        target.setIsbn(source.getIsbn());
        target.setTitle(source.getTitle());
        target.setYear(source.getYear());
        target.setPublisher(source.getPublisher());
        return target;
    }

    public static BookAuditLog mapToBookAuditLog(BookUpdateEvent source) {
        BookAuditLog target = new BookAuditLog();
        target.setActionPerformed(source.getActionPerformed());
        target.setBookId(source.getBookId());
        target.setTimestamp(source.getEventTimestamp());
        target.setPerformedBy(source.getPerformedBy());
        target.setOldValue(source.getOldValue());
        target.setNewValue(source.getNewValue());
        return target;
    }

    public static BookAuditLog mapToBookAuditLog(Book source) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        BookAuditLog target = new BookAuditLog();
        target.setActionPerformed(Constants.CREATE_ACTION);
        target.setTimestamp(LocalDateTime.now());
        target.setBookId(source.getId());
        target.setPerformedBy(authentication.getName());
        return target;
    }

}
