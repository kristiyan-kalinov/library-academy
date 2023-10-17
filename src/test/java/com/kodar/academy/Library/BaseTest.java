package com.kodar.academy.Library;

import com.kodar.academy.Library.model.dto.author.AuthorDTO;
import com.kodar.academy.Library.model.dto.book.BookCreateDTO;
import com.kodar.academy.Library.model.entity.Author;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.entity.Genre;
import com.kodar.academy.Library.model.entity.Rent;
import com.kodar.academy.Library.model.entity.User;
import com.kodar.academy.Library.model.enums.Deactivation;
import com.kodar.academy.Library.model.enums.Role;
import com.kodar.academy.Library.model.mapper.AuthorMapper;
import com.kodar.academy.Library.model.mapper.GenreMapper;
import com.kodar.academy.Library.repository.BookRepository;
import com.kodar.academy.Library.repository.GenreRepository;
import com.kodar.academy.Library.repository.RentRepository;
import com.kodar.academy.Library.repository.UserRepository;
import com.kodar.academy.Library.service.AuthorService;
import com.kodar.academy.Library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BaseTest {

    @Autowired
    protected GenreRepository genreRepository;
    @Autowired
    protected AuthorService authorService;
    @Autowired
    protected BookRepository bookRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    BookService bookService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    protected RentRepository rentRepository;

    protected Author genAuthor1(){
        AuthorDTO author = new AuthorDTO();
        author.setFirstName("ivan");
        author.setLastName("ivanov");

        return authorService.addOrFindAuthor(author);
    }

    protected Author genAuthor2(){
        AuthorDTO author = new AuthorDTO();
        author.setFirstName("asen");
        author.setLastName("arsenov");

        return authorService.addOrFindAuthor(author);
    }

    protected Author genAuthor0() {
        AuthorDTO author = new AuthorDTO();
        author.setFirstName("");
        author.setLastName("");

        return authorService.addOrFindAuthor(author);
    }

    protected Book genBook(int id) {
        Book book = new Book();
        book.setIsbn(id + "-111-111-111-111");
        book.setTitle("kek");
        book.setPublisher("asd");
        book.setYear((short) 2023);
        book.setAvailableQuantity(5);
        book.setTotalQuantity(5);
        book.setIsActive(true);
        book.setDeactivationReason(null);
        book.setDateAdded(LocalDateTime.now());
        Set<Author> authors = new HashSet<>();
        authors.add(genAuthor1());
        authors.add(genAuthor2());
        book.setAuthors(authors);
        Set<Genre> genres = new HashSet<>();
        genres.add(genreRepository.findByName("Adventure").orElseThrow());
        genres.add(genreRepository.findByName("Fantasy").orElseThrow());
        book.setGenres(genres);

        return bookRepository.save(book);
    }

    protected Book genBook0(int id) {
        Book book = new Book();
        book.setIsbn(id + "-111-111-111-111");
        book.setTitle("kek");
        book.setPublisher("asd");
        book.setYear((short) 2023);
        book.setAvailableQuantity(0);
        book.setTotalQuantity(5);
        book.setIsActive(false);
        book.setDeactivationReason(Deactivation.OTHER.toString());
        book.setDateAdded(LocalDateTime.now());
        Set<Author> authors = new HashSet<>();
        authors.add(genAuthor1());
        authors.add(genAuthor2());
        book.setAuthors(authors);
        Set<Genre> genres = new HashSet<>();
        genres.add(genreRepository.findByName("Adventure").orElseThrow());
        genres.add(genreRepository.findByName("Fantasy").orElseThrow());
        book.setGenres(genres);

        return bookRepository.save(book);
    }

    protected BookCreateDTO genBookCreateDTO() {
        BookCreateDTO bookCreateDTO = new BookCreateDTO();
        bookCreateDTO.setIsbn("2-222-222-222-222");
        bookCreateDTO.setTitle("kek");
        bookCreateDTO.setPublisher("asd");
        bookCreateDTO.setYear((short) 2023);
        bookCreateDTO.setTotalQuantity(5);
        Set<Author> authors = new HashSet<>();
        authors.add(genAuthor1());
        authors.add(genAuthor2());
        bookCreateDTO.setAuthors(authors.stream().map(AuthorMapper::mapToResponse).collect(Collectors.toSet()));
        Set<Genre> genres = new HashSet<>();
        genres.add(genreRepository.findByName("Adventure").orElseThrow());
        genres.add(genreRepository.findByName("Fantasy").orElseThrow());
        bookCreateDTO.setGenres(genres.stream().map(GenreMapper::mapToResponse).collect(Collectors.toSet()));
        return bookCreateDTO;
    }

    protected User genAdmin() {
        User user = new User();
        user.setFirstName("arsen");
        user.setLastName("arsenov");
        user.setUsername("arsen");
        user.setDisplayName("arsen");
        user.setPassword(passwordEncoder.encode("test1234"));
        user.setRole(Role.ADMIN);
        return userRepository.save(user);
    }

    protected User genUser() {
        User user = new User();
        user.setFirstName("arsen");
        user.setLastName("arsenov");
        user.setUsername("arsen");
        user.setDisplayName("arsen");
        user.setPassword(passwordEncoder.encode("test1234"));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    protected User genUser1() {
        User user = new User();
        user.setFirstName("ivan");
        user.setLastName("ivanov");
        user.setUsername("ivan");
        user.setDisplayName("ivan");
        user.setPassword(passwordEncoder.encode("test1234"));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    protected User genRentUser() {
        User user = new User();
        user.setFirstName("ali");
        user.setLastName("sali");
        user.setUsername("ali");
        user.setDisplayName("ali");
        user.setPassword(passwordEncoder.encode("test1234"));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    protected Rent genRent(User user, Book book) {
        Rent rent = new Rent();
        rent.setUser(user);
        rent.setBook(book);
        rent.setRentDate(LocalDate.now());
        rent.setReturnDate(null);
        rent.setExpectedReturnDate(LocalDate.now().plusMonths(1));
        user.getRents().add(rent);
        return rentRepository.save(rent);
    }

}
