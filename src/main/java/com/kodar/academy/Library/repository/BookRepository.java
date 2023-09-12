package com.kodar.academy.Library.repository;

import com.kodar.academy.Library.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {

}
