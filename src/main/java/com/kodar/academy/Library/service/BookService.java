package com.kodar.academy.Library.service;

import com.kodar.academy.Library.model.dto.author.AuthorDTO;
import com.kodar.academy.Library.model.dto.book.BookCreateDTO;
import com.kodar.academy.Library.model.dto.book.BookResponseDTO;

import java.util.List;

public interface BookService {

    List<BookResponseDTO> getAllBooks();

    BookResponseDTO getBookById(int id);

    void deleteBook(int id);

    BookResponseDTO addBook(BookCreateDTO bookCreateDTO);

    void addAuthor(AuthorDTO authorDTO);

}
