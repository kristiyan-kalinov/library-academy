package com.kodar.academy.Library.service.impl;

import com.kodar.academy.Library.model.dto.book.BookCreateDTO;
import com.kodar.academy.Library.model.dto.book.BookResponseDTO;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.mapper.BookMapper;
import com.kodar.academy.Library.repository.BookRepository;
import com.kodar.academy.Library.repository.GenreRepository;
import com.kodar.academy.Library.service.AuthorService;
import com.kodar.academy.Library.service.BookService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorService authorService;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, GenreRepository genreRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.authorService = authorService;
    }

    @Override
    public List<BookResponseDTO> getAllBooks() {
        List<BookResponseDTO> books = bookRepository.findAll().stream()
                .map(BookMapper::mapToResponse)
                .toList();
        return books;
    }

    @Override
    public BookResponseDTO getBookById(int id) {
        Optional<Book> bookData = bookRepository.findById(id);
        if(bookData.isPresent()) {
            Book book = bookData.get();
            return BookMapper.mapToResponse(book);
        }
        return null;
    }

    @Override
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BookResponseDTO addBook(BookCreateDTO bookCreateDTO) {
        Book book = BookMapper.mapToBook(bookCreateDTO);
        book.setDateAdded(LocalDateTime.now());
        book.setGenres(bookCreateDTO.getGenres().stream()
                .map(genre -> genreRepository.findByName(genre.getName()).orElse(null))
                .collect(Collectors.toSet()));
        book.setAuthors(bookCreateDTO.getAuthors().stream()
                .map(authorService::addOrFindAuthor)
                .collect(Collectors.toSet()));
        bookRepository.save(book);
        return BookMapper.mapToResponse(book);
    }

}
