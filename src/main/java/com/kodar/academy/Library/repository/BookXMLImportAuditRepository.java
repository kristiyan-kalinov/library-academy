package com.kodar.academy.Library.repository;

import com.kodar.academy.Library.model.entity.BookXMLImportAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookXMLImportAuditRepository extends JpaRepository<BookXMLImportAudit, Integer> {

    @Query("""
            SELECT bx FROM BookXMLImportAudit bx
            WHERE bx.zipFileName = :zipName
            AND bx.xmlFileName = :xmlName
            ORDER BY bx.id DESC
            LIMIT 1
            """)
    Optional<BookXMLImportAudit> findLastAuditByZipNameAndXmlName(@Param("zipName") String zipName,
                                                                 @Param("xmlName") String xmlName);

    @Query("""
            SELECT bx FROM BookXMLImportAudit bx
            WHERE bx.zipFileName = :zipName
            AND bx.xmlFileName IS NULL
            ORDER BY bx.id DESC
            LIMIT 1
            """)
    Optional<BookXMLImportAudit> findLastAuditByZipName(@Param("zipName") String zipName);

}
