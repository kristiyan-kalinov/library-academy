package com.kodar.academy.Library.repository;

import com.kodar.academy.Library.model.entity.BookAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookAuditLogRepository extends JpaRepository<BookAuditLog, Integer> {
}
