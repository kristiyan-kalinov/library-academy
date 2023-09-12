package com.kodar.academy.Library.repository;

import com.kodar.academy.Library.model.entity.BookAuditLog;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookAuditLogRepository extends JpaRepository<BookAuditLog, Integer> {

    @Transactional
    @Modifying
    @Query("""
            UPDATE BookAuditLog b
            SET b.performedBy = null
            WHERE b.performedBy = :userId
            """)
    void setUserIdToNull(@Param("userId") String userId);

    @Transactional
    @Modifying
    @Query("""
            DELETE FROM BookAuditLog b
            WHERE b.bookId = :bookId
            """)
    void deleteAuditWhenDeletingBook(@Param("bookId") int bookId);

}
