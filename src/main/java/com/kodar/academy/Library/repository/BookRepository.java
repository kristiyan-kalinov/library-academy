package com.kodar.academy.Library.repository;

import com.kodar.academy.Library.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    Optional<Book> findByIsbn(String isbn);

}
