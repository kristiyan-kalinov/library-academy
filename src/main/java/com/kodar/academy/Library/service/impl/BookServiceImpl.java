package com.kodar.academy.Library.service.impl;

import com.kodar.academy.Library.model.dto.author.AuthorDTO;
import com.kodar.academy.Library.model.dto.book.BookCreateDTO;
import com.kodar.academy.Library.model.dto.book.BookEditRequestDTO;
import com.kodar.academy.Library.model.dto.book.BookResponseDTO;
import com.kodar.academy.Library.model.entity.Author;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.mapper.BookMapper;
import com.kodar.academy.Library.repository.AuthorRepository;
import com.kodar.academy.Library.repository.BookRepository;
import com.kodar.academy.Library.repository.GenreRepository;
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
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
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
                .map(author -> {
                    addAuthor(author);
                    return authorRepository.findByFirstNameAndLastName(author.getFirstName(), author.getLastName()).orElse(null);
                })
                .collect(Collectors.toSet()));
        bookRepository.save(book);
        return BookMapper.mapToResponse(book);
    }

    //move to author service
    @Override
    public void addAuthor(AuthorDTO authorDTO) {
        if(authorRepository.findByFirstNameAndLastName(authorDTO.getFirstName(), authorDTO.getLastName()).isEmpty()) {
            Author author = new Author();
            author.setFirstName(authorDTO.getFirstName());
            author.setLastName(authorDTO.getLastName());
            authorRepository.save(author);
        };
    }

    @Override
    @Transactional
    public BookResponseDTO editBook(int id, BookEditRequestDTO bookEditRequestDTO) {
        Book oldBook = bookRepository.findById(id).orElseThrow();
        oldBook.setTitle(bookEditRequestDTO.getTitle());
        oldBook.setPublisher(bookEditRequestDTO.getPublisher());
        bookRepository.save(oldBook);
        return BookMapper.mapToResponse(oldBook);
    }

}
