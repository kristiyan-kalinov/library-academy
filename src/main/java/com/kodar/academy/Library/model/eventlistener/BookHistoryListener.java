package com.kodar.academy.Library.model.eventlistener;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.entity.BookAuditLog;
import com.kodar.academy.Library.repository.BookAuditLogRepository;
import jakarta.persistence.PostPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookHistoryListener {

    private final BookAuditLogRepository bookAuditLogRepository;

    @Autowired
    @Lazy
    public BookHistoryListener(BookAuditLogRepository bookAuditLogRepository) {
        this.bookAuditLogRepository = bookAuditLogRepository;
    }

    @PostPersist
    public void onPostPersist(Book book) {
        saveAuditLog(book);
    }

    private void saveAuditLog(Book book) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        BookAuditLog bookAuditLog = new BookAuditLog();
        bookAuditLog.setActionPerformed(Constants.CREATE_ACTION);
        bookAuditLog.setTimestamp(LocalDateTime.now());
        bookAuditLog.setBookId(book.getId());
        bookAuditLog.setPerformedBy(authentication.getName());

        bookAuditLogRepository.save(bookAuditLog);
    }

}
