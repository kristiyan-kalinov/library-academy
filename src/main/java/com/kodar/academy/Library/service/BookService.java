package com.kodar.academy.Library.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.dto.book.BookChangeStatusDTO;
import com.kodar.academy.Library.model.dto.book.BookCreateDTO;
import com.kodar.academy.Library.model.dto.book.BookEditRequestDTO;
import com.kodar.academy.Library.model.dto.book.BookFilterRequest;
import com.kodar.academy.Library.model.dto.book.BookResponseDTO;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.enums.Deactivation;
import com.kodar.academy.Library.model.eventlistener.BookBaseEvent;
import com.kodar.academy.Library.model.eventlistener.BookCreateEvent;
import com.kodar.academy.Library.model.eventlistener.BookUpdateAvailableQuantityEvent;
import com.kodar.academy.Library.model.eventlistener.BookUpdateDeactReasonEvent;
import com.kodar.academy.Library.model.eventlistener.BookUpdatePublisherEvent;
import com.kodar.academy.Library.model.eventlistener.BookUpdateStatusEvent;
import com.kodar.academy.Library.model.eventlistener.BookUpdateTitleEvent;
import com.kodar.academy.Library.model.eventlistener.BookUpdateTotalQuantityEvent;
import com.kodar.academy.Library.model.eventlistener.BookXMLImportFailEvent;
import com.kodar.academy.Library.model.eventlistener.BookXMLImportSuccessEvent;
import com.kodar.academy.Library.model.exceptions.BookNotActiveException;
import com.kodar.academy.Library.model.exceptions.BookNotFoundException;
import com.kodar.academy.Library.model.exceptions.DeleteActiveBookException;
import com.kodar.academy.Library.model.exceptions.InsufficientBookTotalQuantityException;
import com.kodar.academy.Library.model.exceptions.InvalidDeactReasonException;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Service
public class BookService {

    private final XmlMapper xmlMapper;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final BookAuditLogRepository bookAuditLogRepository;
    private final ApplicationEventPublisher publisher;
    private final AuthorService authorService;
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    public BookService(XmlMapper xmlMapper, BookRepository bookRepository, AuthorService authorService,
                       GenreRepository genreRepository, BookAuditLogRepository bookAuditLogRepository,
                       ApplicationEventPublisher publisher) {
        this.xmlMapper = xmlMapper;
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

    @Transactional
    public String xmlImport(String folderPath) throws IOException {
        File folder = new File(folderPath);
        int counter = 0;
        for(File file : folder.listFiles()) {
            if(file.getName().endsWith(".zip")) {
                List<BookCreateDTO> bookCreateDTOs = new ArrayList<>();
                List<BookXMLImportFailEvent> bookXMLImportFailEvents = new ArrayList<>();
                try (ZipFile zipFile = new ZipFile(file)) {
                    Enumeration<? extends ZipEntry> entries = zipFile.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();
                        if(!entry.isDirectory() && entry.getName().endsWith(".xml")) {
                            InputStream inputStream = zipFile.getInputStream(entry);
                            byte[] bytes = inputStream.readAllBytes();
                            String input = new String(bytes, StandardCharsets.UTF_8);
                            BookCreateDTO bookRequest = xmlMapper.readValue(input, BookCreateDTO.class);

                            List<String> messages = new ArrayList<>();
                            if(bookRequest.getIsbn().isBlank()) {
                                messages.add(Constants.ISBN_REQUIRED);
                            }
                            if(bookRepository.findByIsbn(bookRequest.getIsbn()).isPresent()) {
                                messages.add(Constants.DUPLICATE_ISBN);
                            }
                            if(bookRequest.getTitle().isBlank()) {
                                messages.add(Constants.TITLE_REQUIRED);
                            }
                            if(bookRequest.getTitle().isEmpty() || bookRequest.getTitle().length() > 255) {
                                messages.add(Constants.TITLE_LENGTH);
                            }
                            if(bookRequest.getPublisher().isBlank()) {
                                messages.add(Constants.PUBLISHER_REQUIRED);
                            }
                            if(bookRequest.getPublisher().isEmpty() || bookRequest.getPublisher().length() > 255) {
                                messages.add(Constants.PUBLISHER_LENGTH);
                            }
                            bookRequest.getGenres().forEach(genre -> {
                                if(genreRepository.findByName(genre.getName()).isEmpty()) {
                                    messages.add(genre.getName() + " does not exist");
                                }
                            });
                            bookRequest.getAuthors().forEach(author -> {
                                String firstName = author.getFirstName();
                                String lastName = author.getFirstName();
                                if(firstName.isBlank()) {
                                    messages.add(Constants.FNAME_REQUIRED);
                                }
                                if(firstName.isEmpty() || firstName.length()> 255) {
                                    messages.add(Constants.FNAME_LENGTH);
                                }
                                if(!firstName.matches("^[a-zA-Z]+$")) {
                                    messages.add(Constants.FNAME_LETTERS);
                                }
                                if(lastName.isBlank()) {
                                    messages.add(Constants.LNAME_REQUIRED);
                                }
                                if(lastName.isEmpty() || lastName.length()> 255) {
                                    messages.add(Constants.LNAME_LENGTH);
                                }
                                if(!lastName.matches("^[a-zA-Z]+$")) {
                                    messages.add(Constants.LNAME_LETTERS);
                                }
                            });
                            if(bookRequest.getTotalQuantity() < 0) {
                                messages.add(Constants.TOTAL_QUANTITY_MIN_VALUE);
                            }

                            if(messages.isEmpty()) {
                                bookCreateDTOs.add(bookRequest);
                            }
                            else {
                                StringJoiner error = new StringJoiner(";");
                                for(String m : messages) {
                                    error.add(m);
                                }
                                bookXMLImportFailEvents.add(new BookXMLImportFailEvent(file.getName(),
                                        entry.getName(), error.toString()));
                            }
                        }
                    }
                }
                if(bookXMLImportFailEvents.isEmpty()) {
                    for(BookCreateDTO b : bookCreateDTOs) {
                        counter++;
                        addBook(b);
                    }
                    publisher.publishEvent(new BookXMLImportSuccessEvent(file.getName(), null));
                    Path from = Paths.get(file.getPath());
                    Path to = Paths.get("src/main/resources/imported-zips/" + file.getName());
                    Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
                }
                else {
                    for(BookXMLImportFailEvent fail : bookXMLImportFailEvents) {
                        publisher.publishEvent(fail);
                    }
                }
            }
        }
        return counter > 0 ? "Successfully imported " + counter + " books" : "Fail";
    }
}
