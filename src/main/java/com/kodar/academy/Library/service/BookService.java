package com.kodar.academy.Library.service;

import com.kodar.academy.Library.model.dto.book.*;
import com.kodar.academy.Library.model.entity.Book;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface BookService {
    List<BookResponseDTO> getAllBooks(BookFilterRequest bookFilterRequest);

    BookResponseDTO getBookById(int id) throws Exception;

    void deleteBook(int id) throws Exception;

    BookResponseDTO addBook(BookCreateDTO bookCreateDTO);

    BookResponseDTO editBook(int id, BookEditRequestDTO bookEditRequestDTO);

    Specification<Book> getSpecs(BookFilterRequest bookFilterRequest);

    BookResponseDTO changeStatus(int id, BookChangeStatusDTO bookChangeStatusDTO);
}
