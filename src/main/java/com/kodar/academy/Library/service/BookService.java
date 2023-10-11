package com.kodar.academy.Library.service;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.dto.book.*;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.enums.Deactivation;
import com.kodar.academy.Library.model.eventlistener.*;
import com.kodar.academy.Library.model.exceptions.*;
import com.kodar.academy.Library.model.mapper.BookMapper;
import com.kodar.academy.Library.model.specifications.Specs;
import com.kodar.academy.Library.repository.BookAuditLogRepository;
import com.kodar.academy.Library.repository.BookRepository;
import com.kodar.academy.Library.repository.GenreRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final BookAuditLogRepository bookAuditLogRepository;
    private final ApplicationEventPublisher publisher;
    private final AuthorService authorService;
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    public BookService(BookRepository bookRepository, AuthorService authorService,
                           GenreRepository genreRepository, BookAuditLogRepository bookAuditLogRepository,
                           ApplicationEventPublisher publisher) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.bookAuditLogRepository = bookAuditLogRepository;
        this.publisher = publisher;
        this.authorService = authorService;
    }

    public List<BookResponseDTO> getAllBooks(BookFilterRequest bookFilterRequest) {
        logger.info("getAllBooks called");
        List<Book> books = (bookFilterRequest == null)
                ? bookRepository.findAll()
                : bookRepository.findAll(getSpecs(bookFilterRequest));
        return books.stream()
                .map(BookMapper::mapToResponse)
                .toList();
    }

    public BookResponseDTO getBookById(int id) {
        logger.info("getBookById called with params: " + id);
        Book book = bookRepository.findById(id).orElse(null);
        if(book != null) {
            if(book.getIsActive()) {
                return BookMapper.mapToResponse(book);
            }
            else throw new BookNotActiveException(id);
        }
        else throw new BookNotFoundException(id);
    }

    @Transactional
    public void deleteBook(int id) {
        logger.info("deleteBook called with params: " + id);
        Book book = bookRepository.findById(id).orElse(null);
        if(book != null) {
            if(!book.getIsActive()) {
                bookAuditLogRepository.deleteAuditWhenDeletingBook(id);
                bookRepository.deleteById(id);
            }
            else throw new DeleteActiveBookException();
        }
        else throw new BookNotFoundException(id);
    }

    @Transactional
    public BookResponseDTO addBook(BookCreateDTO bookCreateDTO) {
        logger.info("addBook called with params: " + bookCreateDTO.toString());
        Book book = BookMapper.mapToBook(bookCreateDTO);
        book.setIsActive(true);
        book.setDateAdded(LocalDateTime.now());
        book.setGenres(bookCreateDTO.getGenres().stream()
                .map(genre -> genreRepository.findByName(genre.getName()).orElse(null))
                .collect(Collectors.toSet()));
        book.setAuthors(bookCreateDTO.getAuthors().stream()
                .map(authorService::addOrFindAuthor)
                .collect(Collectors.toSet()));
        Book savedBook = bookRepository.save(book);
        publisher.publishEvent(new BookCreateEvent(savedBook));
        return BookMapper.mapToResponse(book);
    }

    @Transactional
    public BookResponseDTO editBook(int id, BookEditRequestDTO bookEditRequestDTO) {
        logger.info("editBook called for book with id: " + id + " and params: " + bookEditRequestDTO.toString());
        Book book = bookRepository.findById(id).orElse(null);
        if(book == null) {
            throw new BookNotFoundException(id);
        }
        String oldTitle = book.getTitle();
        String oldPublisher = book.getPublisher();
        int oldTotalQuantity = book.getTotalQuantity();
        int oldAvailableQuantity = book.getAvailableQuantity();
        int newTotalQuantity = bookEditRequestDTO.getTotalQuantity();
        List<BookBaseEvent> bookUpdateEvents = new ArrayList<>();
        if(!oldTitle.equals(bookEditRequestDTO.getTitle())) {
            book.setTitle(bookEditRequestDTO.getTitle());
            bookUpdateEvents.add(new BookUpdateTitleEvent(oldTitle, book));
        }
        if(!oldPublisher.equals(bookEditRequestDTO.getPublisher())) {
            book.setPublisher(bookEditRequestDTO.getPublisher());
            bookUpdateEvents.add(new BookUpdatePublisherEvent(oldPublisher, book));
        }
        if(oldTotalQuantity != newTotalQuantity) {
            int diff = Math.abs(oldTotalQuantity - newTotalQuantity);
            if(oldTotalQuantity > newTotalQuantity) {
                if(diff > oldAvailableQuantity) {
                    throw new InsufficientBookTotalQuantityException();
                }
                book.setAvailableQuantity(oldAvailableQuantity - diff);
            }
            else {
                book.setAvailableQuantity(oldAvailableQuantity + diff);
            }
            book.setTotalQuantity(newTotalQuantity);
            bookUpdateEvents.add(new BookUpdateTotalQuantityEvent(String.valueOf(oldTotalQuantity), book));
            bookUpdateEvents.add(new BookUpdateAvailableQuantityEvent(String.valueOf(oldAvailableQuantity), book));
        }
        if(!bookUpdateEvents.isEmpty()) {
            bookRepository.save(book);
            for(BookBaseEvent bue : bookUpdateEvents) {
                publisher.publishEvent(bue);
            }
        }
        return BookMapper.mapToResponse(book);
    }

    private Specification<Book> getSpecs(BookFilterRequest bookFilterRequest) {
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

    public BookResponseDTO changeStatus(int id, BookChangeStatusDTO bookChangeStatusDTO) {
        logger.info("changeStatus called for book with id: " + id + " and params: " + bookChangeStatusDTO.toString());
        Book book = bookRepository.findById(id).orElse(null);
        if(book == null) {
            throw new BookNotFoundException(id);
        }
        boolean oldStatus = book.getIsActive();
        String oldReason = book.getDeactivationReason();
        List<BookBaseEvent> bookBaseEvents = new ArrayList<>();
        if(oldStatus != bookChangeStatusDTO.getIsActive()) {
            if(!bookChangeStatusDTO.getIsActive()) {
                if(!EnumUtils.isValidEnum(Deactivation.class, bookChangeStatusDTO.getDeactivationReason())) {
                    throw new InvalidDeactReasonException();
                }
                book.setIsActive(bookChangeStatusDTO.getIsActive());
                book.setDeactivationReason(bookChangeStatusDTO.getDeactivationReason());
            }
            else {
                book.setIsActive(bookChangeStatusDTO.getIsActive());
                book.setDeactivationReason(null);
            }
            bookBaseEvents.add(new BookUpdateStatusEvent(String.valueOf(oldStatus), book));
            bookBaseEvents.add(new BookUpdateDeactReasonEvent(oldReason, book));
            bookRepository.save(book);
            for(BookBaseEvent bue : bookBaseEvents) {
                publisher.publishEvent(bue);
            }
        }
        return BookMapper.mapToResponse(book);
    }
}
