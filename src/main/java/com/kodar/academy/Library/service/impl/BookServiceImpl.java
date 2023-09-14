package com.kodar.academy.Library.service.impl;

import com.kodar.academy.Library.model.dto.book.BookCreateDTO;
import com.kodar.academy.Library.model.dto.book.BookEditRequestDTO;
import com.kodar.academy.Library.model.dto.book.BookResponseDTO;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.eventlistener.BookUpdateEvent;
import com.kodar.academy.Library.model.eventlistener.BookUpdatePublisherEvent;
import com.kodar.academy.Library.model.eventlistener.BookUpdateTitleEvent;
import com.kodar.academy.Library.model.mapper.BookMapper;
import com.kodar.academy.Library.repository.BookAuditLogRepository;
import com.kodar.academy.Library.repository.BookRepository;
import com.kodar.academy.Library.repository.GenreRepository;
import com.kodar.academy.Library.service.AuthorService;
import com.kodar.academy.Library.service.BookService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final BookAuditLogRepository bookAuditLogRepository;
    private final ApplicationEventPublisher publisher;
    private final AuthorService authorService;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService,
                           GenreRepository genreRepository, BookAuditLogRepository bookAuditLogRepository,
                           ApplicationEventPublisher publisher) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.bookAuditLogRepository = bookAuditLogRepository;
        this.publisher = publisher;
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
    @Transactional
    public void deleteBook(int id) {
        bookAuditLogRepository.deleteAuditWhenDeletingBook(id);
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

    @Override
    @Transactional
    public BookResponseDTO editBook(int id, BookEditRequestDTO bookEditRequestDTO) {
        Book oldBook = bookRepository.findById(id).orElseThrow();
        String oldTitle = oldBook.getTitle();
        String oldPublisher = oldBook.getPublisher();
        oldBook.setTitle(bookEditRequestDTO.getTitle());
        oldBook.setPublisher(bookEditRequestDTO.getPublisher());
        List<BookUpdateEvent> bookUpdateEvents = new ArrayList<>();
        if(!oldTitle.equals(bookEditRequestDTO.getTitle())) {
            bookUpdateEvents.add(new BookUpdateTitleEvent(oldTitle, oldBook));
        }
        if(!oldPublisher.equals(bookEditRequestDTO.getPublisher())) {
            bookUpdateEvents.add(new BookUpdatePublisherEvent(oldPublisher, oldBook));
        }
        if(!bookUpdateEvents.isEmpty()) {
            bookRepository.save(oldBook);
            for(BookUpdateEvent bue : bookUpdateEvents) {
                publisher.publishEvent(bue);
            }
        }
        return BookMapper.mapToResponse(oldBook);
    }
}
