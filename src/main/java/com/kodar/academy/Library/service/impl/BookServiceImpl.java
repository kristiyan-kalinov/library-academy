package com.kodar.academy.Library.service.impl;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.dto.book.*;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.eventlistener.*;
import com.kodar.academy.Library.model.mapper.BookMapper;
import com.kodar.academy.Library.model.specifications.Specs;
import com.kodar.academy.Library.repository.BookAuditLogRepository;
import com.kodar.academy.Library.repository.BookRepository;
import com.kodar.academy.Library.repository.GenreRepository;
import com.kodar.academy.Library.service.AuthorService;
import com.kodar.academy.Library.service.BookService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.domain.Specification;
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
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

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
    public List<BookResponseDTO> getAllBooks(BookFilterRequest bookFilterRequest) {
        logger.info("getAllBooks called");
        List<BookResponseDTO> books = bookRepository.findAll(getSpecs(bookFilterRequest)).stream()
                .map(BookMapper::mapToResponse)
                .toList();
        return books;
    }

    @Override
    public BookResponseDTO getBookById(int id) throws Exception {
        logger.info("getBookById called with params: " + id);
        Book book = bookRepository.findById(id).orElseThrow();
        if(book.getIsActive()) {
            return BookMapper.mapToResponse(book);
        }
        else throw new Exception("Inactive books can't be accessed");
    }

    @Override
    @Transactional
    public void deleteBook(int id) throws Exception {
        logger.info("deleteBook called with params: " + id);
        Optional<Book> book = bookRepository.findById(id);
        if(book.isPresent()) {
            if(!book.get().getIsActive()) {
                bookAuditLogRepository.deleteAuditWhenDeletingBook(id);
                bookRepository.deleteById(id);
            }
            else throw new Exception("Active books can't be deleted");
        }
        throw new Exception("Book with that id does not exist");
    }

    @Override
    @Transactional
    public BookResponseDTO addBook(BookCreateDTO bookCreateDTO) {
        logger.info("addBook called with params: " + bookCreateDTO.toString());
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
        logger.info("editBook called for book with id: " + id + " and params: " + bookEditRequestDTO.toString());
        Book oldBook = bookRepository.findById(id).orElseThrow();
        String oldTitle = oldBook.getTitle();
        String oldPublisher = oldBook.getPublisher();
        List<BookUpdateEvent> bookUpdateEvents = new ArrayList<>();
        if(!oldTitle.equals(bookEditRequestDTO.getTitle())) {
            oldBook.setTitle(bookEditRequestDTO.getTitle());
            bookUpdateEvents.add(new BookUpdateTitleEvent(oldTitle, oldBook));
        }
        if(!oldPublisher.equals(bookEditRequestDTO.getPublisher())) {
            oldBook.setPublisher(bookEditRequestDTO.getPublisher());
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

    @Override
    public Specification<Book> getSpecs(BookFilterRequest bookFilterRequest) {
        logger.info("getSpecs called with params: " + bookFilterRequest.toString());
        Specification<Book> specification = null;
        if(bookFilterRequest.getTitle() != null) {
            specification = Specs.fieldLike(Constants.TITLE, bookFilterRequest.getTitle()).and(specification);
        }
        if(bookFilterRequest.getPublisher() != null) {
            specification = Specs.fieldLike(Constants.PUBLISHER, bookFilterRequest.getPublisher()).and(specification);
        }
        if(bookFilterRequest.getYearAfter() != null) {
            specification = Specs.yearAfter(bookFilterRequest.getYearAfter()).and(specification);
        }
        if(bookFilterRequest.getYearBefore() != null) {
            specification = Specs.yearBefore(bookFilterRequest.getYearBefore()).and(specification);
        }
        if(bookFilterRequest.getGenres() != null) {
            for (int i = 0; i < bookFilterRequest.getGenres().length; i++) {
                specification = Specs.hasGenre(bookFilterRequest.getGenres()[i]).and(specification);
            }
        }
        if(bookFilterRequest.getAuthorFirstName() != null) {
            for (int i = 0; i < bookFilterRequest.getAuthorFirstName().length; i++) {
                specification = Specs.nameEqual(Constants.FIRST_NAME, bookFilterRequest.getAuthorFirstName()[i]).and(specification);
            }
        }
        if(bookFilterRequest.getAuthorLastName() != null) {
            for (int i = 0; i < bookFilterRequest.getAuthorLastName().length; i++) {
                specification = Specs.nameEqual(Constants.LAST_NAME, bookFilterRequest.getAuthorLastName()[i]).and(specification);
            }
        }
        if(!bookFilterRequest.getShowAll()) {
            specification = Specs.isActive(true).and(specification);
        }
        return specification;
    }

    @Override
    public BookResponseDTO changeStatus(int id, BookChangeStatusDTO bookChangeStatusDTO) {
        logger.info("changeStatus called for book with id: " + id + " and params: " + bookChangeStatusDTO.toString());
        Book oldBook = bookRepository.findById(id).orElseThrow();
        boolean oldStatus = oldBook.getIsActive();
        String oldReason = oldBook.getDeactivationReason();
        List<BookUpdateEvent> bookUpdateEvents = new ArrayList<>();
        if(oldStatus != bookChangeStatusDTO.getIsActive()) {
            if(!bookChangeStatusDTO.getIsActive()) {
                oldBook.setIsActive(bookChangeStatusDTO.getIsActive());
                oldBook.setDeactivationReason(bookChangeStatusDTO.getDeactivationReason().toString());
            }
            else {
                oldBook.setIsActive(bookChangeStatusDTO.getIsActive());
                oldBook.setDeactivationReason(null);
            }
            bookUpdateEvents.add(new BookUpdateStatusEvent(String.valueOf(oldStatus), oldBook));
            bookUpdateEvents.add(new BookUpdateDeactReasonEvent(oldReason, oldBook));
            bookRepository.save(oldBook);
            for(BookUpdateEvent bue : bookUpdateEvents) {
                publisher.publishEvent(bue);
            }
        }
        return BookMapper.mapToResponse(oldBook);
    }
}
