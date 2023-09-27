package com.kodar.academy.Library.service;

import com.kodar.academy.Library.model.dto.book.*;

import java.util.List;

public interface BookService {
    List<BookResponseDTO> getAllBooks(BookFilterRequest bookFilterRequest);

    BookResponseDTO getBookById(int id) throws Exception;

    void deleteBook(int id) throws Exception;

    BookResponseDTO addBook(BookCreateDTO bookCreateDTO);

    BookResponseDTO editBook(int id, BookEditRequestDTO bookEditRequestDTO);

    BookResponseDTO changeStatus(int id, BookChangeStatusDTO bookChangeStatusDTO);
}
