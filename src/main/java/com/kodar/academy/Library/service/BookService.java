package com.kodar.academy.Library.service;

import com.kodar.academy.Library.model.dto.book.BookCreateDTO;
import com.kodar.academy.Library.model.dto.book.BookEditRequestDTO;
import com.kodar.academy.Library.model.dto.book.BookFilterRequest;
import com.kodar.academy.Library.model.dto.book.BookResponseDTO;
import com.kodar.academy.Library.model.entity.Book;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface BookService {
    List<BookResponseDTO> getAllBooks(BookFilterRequest bookFilterRequest);

    BookResponseDTO getBookById(int id);

    void deleteBook(int id);

    BookResponseDTO addBook(BookCreateDTO bookCreateDTO);

    BookResponseDTO editBook(int id, BookEditRequestDTO bookEditRequestDTO);

    Specification<Book> getSpecs(BookFilterRequest bookFilterRequest);
}
