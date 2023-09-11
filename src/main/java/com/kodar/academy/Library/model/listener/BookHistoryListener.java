package com.kodar.academy.Library.model.listener;

import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.entity.BookAuditLog;
import com.kodar.academy.Library.repository.BookAuditLogRepository;
import com.kodar.academy.Library.repository.BookRepository;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookHistoryListener {

    private final BookAuditLogRepository bookAuditLogRepository;
    private final BookRepository bookRepository;

    @Autowired
    @Lazy
    public BookHistoryListener(BookAuditLogRepository bookAuditLogRepository, BookRepository bookRepository) {
        this.bookAuditLogRepository = bookAuditLogRepository;
        this.bookRepository = bookRepository;
    }

    @PostPersist
    public void onPostPersist(Book book) {
        saveAuditLog(book, "CREATE");
    }

    @PreUpdate
    public void onPreUpdate(Book book) {
        saveAuditLog(book, "UPDATE");
    }

    private void saveAuditLog(Book book, String action) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        BookAuditLog bookAuditLog = new BookAuditLog();
        bookAuditLog.setActionPerformed(action);
        bookAuditLog.setTimestamp(LocalDateTime.now());
        bookAuditLog.setBookId(book.getId());
        bookAuditLog.setPerformedBy(authentication.getName());

        if(action.equals("UPDATE")) {
            Book oldBook = bookRepository.findById(book.getId()).orElse(null);
            if(oldBook != null) {
                bookAuditLog.setOldTitleValue(oldBook.getTitle());
                bookAuditLog.setNewTitleValue(book.getTitle());
                bookAuditLog.setOldPublisherValue(oldBook.getPublisher());
                bookAuditLog.setNewPublisherValue(book.getPublisher());
            }
        }

        bookAuditLogRepository.save(bookAuditLog);
    }

}
