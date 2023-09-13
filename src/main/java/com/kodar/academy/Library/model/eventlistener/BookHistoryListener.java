package com.kodar.academy.Library.model.eventlistener;

import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.entity.BookAuditLog;
import com.kodar.academy.Library.model.mapper.BookMapper;
import com.kodar.academy.Library.repository.BookAuditLogRepository;
import jakarta.persistence.PostPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

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
        BookAuditLog bookAuditLog = BookMapper.mapToBookAuditLog(book);
        bookAuditLogRepository.save(bookAuditLog);
    }

}
