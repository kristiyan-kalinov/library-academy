package com.kodar.academy.Library;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.dto.book.BookChangeStatusDTO;
import com.kodar.academy.Library.model.dto.book.BookCreateDTO;
import com.kodar.academy.Library.model.dto.book.BookEditRequestDTO;
import com.kodar.academy.Library.model.dto.book.BookResponseDTO;
import com.kodar.academy.Library.model.dto.genre.GenreDTO;
import com.kodar.academy.Library.model.dto.rent.RentCreateDTO;
import com.kodar.academy.Library.model.dto.rent.RentResponseDTO;
import com.kodar.academy.Library.model.entity.Author;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.entity.BookXMLImportAudit;
import com.kodar.academy.Library.model.entity.Genre;
import com.kodar.academy.Library.model.entity.Rent;
import com.kodar.academy.Library.model.entity.User;
import com.kodar.academy.Library.model.enums.Role;
import com.kodar.academy.Library.model.exceptions.ErrorMessage;
import com.kodar.academy.Library.model.mapper.AuthorMapper;
import com.kodar.academy.Library.model.mapper.GenreMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class BookControllerIntegrationTest extends BaseTest {

    private static final Set<String> bookEditValidationMessages = new HashSet<>() {{
        add(Constants.TITLE_REQUIRED);
        add(Constants.TITLE_LENGTH);
        add(Constants.PUBLISHER_REQUIRED);
        add(Constants.PUBLISHER_LENGTH);
        add(Constants.TOTAL_QUANTITY_MIN_VALUE);
    }};
    private static final Set<String> bookAddValidationMessages = new HashSet<>() {{
        add(Constants.TITLE_REQUIRED);
        add(Constants.TITLE_LENGTH);
        add(Constants.PUBLISHER_REQUIRED);
        add(Constants.PUBLISHER_LENGTH);
        add(Constants.TOTAL_QUANTITY_MIN_VALUE);
        add(Constants.ISBN_REQUIRED);
        add(Constants.FNAME_REQUIRED);
        add(Constants.FNAME_LENGTH);
        add(Constants.FNAME_LETTERS);
        add(Constants.LNAME_REQUIRED);
        add(Constants.LNAME_LENGTH);
        add(Constants.LNAME_LETTERS);
    }};

    //changeStatus
    @Test
    @Transactional
    void changeStatusBook_ActiveToInactive_Success() throws Exception {
        Book book = genBook(1);
        User user = genAdmin();
        BookChangeStatusDTO bookChangeStatusDTO = new BookChangeStatusDTO();
        bookChangeStatusDTO.setIsActive(false);
        bookChangeStatusDTO.setDeactivationReason("OTHER");

        MockHttpServletResponse result = this.mockMvc.perform(put("/books/change-status/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookChangeStatusDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        BookResponseDTO response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);

        Assertions.assertEquals(bookChangeStatusDTO.getIsActive(), book.getIsActive());
        Assertions.assertEquals(bookChangeStatusDTO.getDeactivationReason(), book.getDeactivationReason());

        Assertions.assertEquals(response.getIsActive(), book.getIsActive());
        Assertions.assertEquals(response.getDeactivationReason(), book.getDeactivationReason());
        Assertions.assertEquals(response.getTitle(), book.getTitle());
        Assertions.assertEquals(response.getIsbn(), book.getIsbn());
        Assertions.assertEquals(response.getPublisher(), book.getPublisher());
        Assertions.assertEquals(response.getYear(), book.getYear());
        Assertions.assertEquals(response.getAvailableQuantity(), book.getAvailableQuantity());
        Assertions.assertEquals(response.getTotalQuantity(), book.getTotalQuantity());
        Assertions.assertEquals(response.getGenres().size(), book.getGenres().size());
        Assertions.assertEquals(response.getAuthors().size(), book.getAuthors().size());
        Assertions.assertTrue(response.getAuthors().stream().map(a -> a.getFirstName() + a.getLastName()).toList()
                        .containsAll(book.getAuthors().stream().map(a -> a.getFirstName() + a.getLastName()).toList()));
        Assertions.assertTrue(response.getGenres().stream().map(GenreDTO::getName).toList()
                        .containsAll(book.getGenres().stream().map(Genre::getName).toList()));
    }

    @Test
    @Transactional
    void changeStatusBook_InactiveToActive_Success() throws Exception {
        Book book = genBook0(1);
        User user = genAdmin();
        BookChangeStatusDTO bookChangeStatusDTO = new BookChangeStatusDTO();
        bookChangeStatusDTO.setIsActive(true);
        bookChangeStatusDTO.setDeactivationReason(null);

        MockHttpServletResponse result = this.mockMvc.perform(put("/books/change-status/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookChangeStatusDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        BookResponseDTO response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);

        Assertions.assertEquals(bookChangeStatusDTO.getIsActive(), book.getIsActive());
        Assertions.assertEquals(bookChangeStatusDTO.getDeactivationReason(), book.getDeactivationReason());

        Assertions.assertEquals(response.getIsActive(), book.getIsActive());
        Assertions.assertEquals(response.getDeactivationReason(), book.getDeactivationReason());
        Assertions.assertEquals(response.getTitle(), book.getTitle());
        Assertions.assertEquals(response.getIsbn(), book.getIsbn());
        Assertions.assertEquals(response.getPublisher(), book.getPublisher());
        Assertions.assertEquals(response.getYear(), book.getYear());
        Assertions.assertEquals(response.getAvailableQuantity(), book.getAvailableQuantity());
        Assertions.assertEquals(response.getTotalQuantity(), book.getTotalQuantity());
        Assertions.assertEquals(response.getGenres().size(), book.getGenres().size());
        Assertions.assertEquals(response.getAuthors().size(), book.getAuthors().size());
        Assertions.assertTrue(response.getAuthors().stream().map(a -> a.getFirstName() + a.getLastName()).toList()
                .containsAll(book.getAuthors().stream().map(a -> a.getFirstName() + a.getLastName()).toList()));
        Assertions.assertTrue(response.getGenres().stream().map(GenreDTO::getName).toList()
                .containsAll(book.getGenres().stream().map(Genre::getName).toList()));
    }

    @Test
    @Transactional
    void changeStatusBook_ForbiddenWhenNotAdmin() throws Exception {
        Book book = genBook(1);
        User user = genUser();
        BookChangeStatusDTO bookChangeStatusDTO = new BookChangeStatusDTO();
        bookChangeStatusDTO.setIsActive(false);
        bookChangeStatusDTO.setDeactivationReason("OTHER");

        MockHttpServletResponse result = this.mockMvc.perform(put("/books/change-status/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookChangeStatusDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @Transactional
    void changeStatusBook_InvalidReason() throws Exception{
        Book book = genBook(1);
        User user = genAdmin();
        BookChangeStatusDTO bookChangeStatusDTO = new BookChangeStatusDTO();
        bookChangeStatusDTO.setIsActive(false);
        bookChangeStatusDTO.setDeactivationReason("test");

        MockHttpServletResponse result = this.mockMvc.perform(put("/books/change-status/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookChangeStatusDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getMessages().get(0), Constants.INVALID_DEACT_REASON);
    }

    @Test
    @Transactional
    void changeStatusBook_BookNotFoundException() throws Exception{
        genBook(1);
        User user = genAdmin();
        BookChangeStatusDTO bookChangeStatusDTO = new BookChangeStatusDTO();
        bookChangeStatusDTO.setIsActive(false);
        bookChangeStatusDTO.setDeactivationReason("OTHER");

        MockHttpServletResponse result = this.mockMvc.perform(put("/books/change-status/" + -5)
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookChangeStatusDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.BOOK_NOT_FOUND, -5), response.getMessages().get(0));
    }

    //addBook
    @Test
    @Transactional
    void addBook_Success() throws Exception {
        BookCreateDTO bookCreateDTO = genBookCreateDTO();
        User user = genAdmin();

        MockHttpServletResponse result = this.mockMvc.perform(post("/books")
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse();

        BookResponseDTO response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });
        Book savedBook = bookRepository.findByIsbn(bookCreateDTO.getIsbn()).orElse(null);

        Assertions.assertNotNull(savedBook);
        Assertions.assertNotNull(response);

        Assertions.assertEquals(bookCreateDTO.getIsbn(), savedBook.getIsbn());
        Assertions.assertEquals(bookCreateDTO.getTitle(), savedBook.getTitle());
        Assertions.assertEquals(bookCreateDTO.getPublisher(), savedBook.getPublisher());
        Assertions.assertEquals(bookCreateDTO.getYear(), savedBook.getYear());
        Assertions.assertEquals(bookCreateDTO.getTotalQuantity(), savedBook.getTotalQuantity());
        Assertions.assertEquals(bookCreateDTO.getTotalQuantity(), savedBook.getAvailableQuantity());
        Assertions.assertTrue(bookCreateDTO.getAuthors().stream().map(a -> a.getFirstName() + a.getLastName()).toList()
                .containsAll(savedBook.getAuthors().stream().map(a -> a.getFirstName() + a.getLastName()).toList()));
        Assertions.assertTrue(bookCreateDTO.getGenres().stream().map(GenreDTO::getName).toList()
                .containsAll(savedBook.getGenres().stream().map(Genre::getName).toList()));

        Assertions.assertEquals(response.getIsbn(), savedBook.getIsbn());
        Assertions.assertEquals(response.getTitle(), savedBook.getTitle());
        Assertions.assertEquals(response.getPublisher(), savedBook.getPublisher());
        Assertions.assertEquals(response.getYear(), savedBook.getYear());
        Assertions.assertEquals(response.getAvailableQuantity(), savedBook.getAvailableQuantity());
        Assertions.assertEquals(response.getTotalQuantity(), savedBook.getTotalQuantity());
        Assertions.assertEquals(response.getIsActive(), savedBook.getIsActive());
        Assertions.assertEquals(response.getDeactivationReason(), savedBook.getDeactivationReason());
        Assertions.assertEquals(savedBook.getGenres().size(), response.getGenres().size());
        Assertions.assertEquals(savedBook.getAuthors().size(), response.getAuthors().size());
        Assertions.assertTrue(response.getAuthors().stream().map(a -> a.getFirstName() + a.getLastName()).toList()
                .containsAll(savedBook.getAuthors().stream().map(a -> a.getFirstName() + a.getLastName()).toList()));
        Assertions.assertTrue(response.getGenres().stream().map(GenreDTO::getName).toList()
                .containsAll(savedBook.getGenres().stream().map(Genre::getName).toList()));
    }

    @Test
    @Transactional
    void addBook_ForbiddenWhenNotAdmin() throws Exception {
        BookCreateDTO bookCreateDTO = genBookCreateDTO();
        User user = genUser();

        MockHttpServletResponse result = this.mockMvc.perform(post("/books")
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @Transactional
    void addBook_ValidationError() throws Exception {
        BookCreateDTO bookCreateDTO = new BookCreateDTO();
        bookCreateDTO.setIsbn("");
        bookCreateDTO.setTitle("");
        bookCreateDTO.setPublisher("");
        bookCreateDTO.setYear((short) 2000);
        bookCreateDTO.setTotalQuantity(-5);
        Set<Genre> genres = new HashSet<>();
        genres.add(genreRepository.findByName("Adventure").orElseThrow());
        genres.add(genreRepository.findByName("Fantasy").orElseThrow());
        bookCreateDTO.setGenres(genres.stream().map(GenreMapper::mapToResponse).collect(Collectors.toSet()));
        Set<Author> authors = new HashSet<>();
        authors.add(genAuthor0());
        bookCreateDTO.setAuthors(authors.stream().map(AuthorMapper::mapToResponse).collect(Collectors.toSet()));
        User user = genAdmin();

        MockHttpServletResponse result = this.mockMvc.perform(post("/books")
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertTrue(bookAddValidationMessages.containsAll(response.getMessages()));
    }

    //deleteBook
    @Test
    @Transactional
    void deleteBook_Success() throws Exception {
        Book book = genBook0(1);
        Assertions.assertNotNull(bookRepository.findById(book.getId()).orElse(null));
        User user = genAdmin();

        MockHttpServletResponse result = this.mockMvc.perform(delete("/books/delete/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        Assertions.assertEquals(Constants.SUCCESSFUL_BOOK_DELETE, result.getContentAsString());
        Assertions.assertNull(bookRepository.findById(book.getId()).orElse(null));
    }

    @Test
    @Transactional
    void deleteBook_ForbiddenWhenNotAdmin() throws Exception {
        Book book = genBook0(1);
        Assertions.assertNotNull(bookRepository.findById(book.getId()).orElse(null));
        User user = genUser();

        MockHttpServletResponse result = this.mockMvc.perform(delete("/books/delete/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @Transactional
    void deleteBook_ThrowExceptionWhenBookIsActive() throws Exception {
        Book book = genBook(1);
        Assertions.assertNotNull(bookRepository.findById(book.getId()).orElse(null));
        User user = genAdmin();

        MockHttpServletResponse result = this.mockMvc.perform(delete("/books/delete/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString()))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Constants.DELETE_ACTIVE_BOOK_MSG, response.getMessages().get(0));
    }

    @Test
    @Transactional
    void deleteBook_NonExistingBook() throws Exception{
        User user = genAdmin();

        MockHttpServletResponse result = this.mockMvc.perform(delete("/books/delete/" + -1)
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString()))))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.BOOK_NOT_FOUND, -1), response.getMessages().get(0));
    }

    //getById
    @Test
    @Transactional
    void getById_Success() throws Exception {
        Book book = genBook(1);

        MockHttpServletResponse result = this.mockMvc.perform(get("/books/" + book.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        BookResponseDTO response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getIsActive(), book.getIsActive());
        Assertions.assertEquals(response.getDeactivationReason(), book.getDeactivationReason());
        Assertions.assertEquals(response.getTitle(), book.getTitle());
        Assertions.assertEquals(response.getIsbn(), book.getIsbn());
        Assertions.assertEquals(response.getPublisher(), book.getPublisher());
        Assertions.assertEquals(response.getYear(), book.getYear());
        Assertions.assertEquals(response.getAvailableQuantity(), book.getAvailableQuantity());
        Assertions.assertEquals(response.getTotalQuantity(), book.getTotalQuantity());
        Assertions.assertEquals(response.getGenres().size(), book.getGenres().size());
        Assertions.assertEquals(response.getAuthors().size(), book.getAuthors().size());
        Assertions.assertTrue(response.getAuthors().stream().map(a -> a.getFirstName() + a.getLastName()).toList()
                .containsAll(book.getAuthors().stream().map(a -> a.getFirstName() + a.getLastName()).toList()));
        Assertions.assertTrue(response.getGenres().stream().map(GenreDTO::getName).toList()
                .containsAll(book.getGenres().stream().map(Genre::getName).toList()));
    }

    @Test
    @Transactional
    void getById_BookNotActiveException() throws Exception {
        Book book = genBook0(1);

        MockHttpServletResponse result = this.mockMvc.perform(get("/books/" + book.getId()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.BOOK_NOT_ACTIVE, book.getId()), response.getMessages().get(0));
    }

    @Test
    @Transactional
    void getById_BookNotFoundException() throws Exception {
        genBook(1);

        MockHttpServletResponse result = this.mockMvc.perform(get("/books/" + -1))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.BOOK_NOT_FOUND, -1), response.getMessages().get(0));
    }

    //editBook
    @Test
    @Transactional
    void editBook_IncreaseQuantity_Success() throws Exception{
        Book book = genBook(1);
        User user = genAdmin();
        BookEditRequestDTO bookEditRequestDTO = new BookEditRequestDTO();
        bookEditRequestDTO.setTitle("edit book test title");
        bookEditRequestDTO.setPublisher("edit book test publisher");
        bookEditRequestDTO.setTotalQuantity(10);
        int oldAvailableQuantity = book.getAvailableQuantity();
        int oldTotalQuantity = book.getTotalQuantity();

        MockHttpServletResponse result = this.mockMvc.perform(put("/books/edit/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookEditRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        BookResponseDTO response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);

        Assertions.assertEquals(bookEditRequestDTO.getTitle(), book.getTitle());
        Assertions.assertEquals(bookEditRequestDTO.getPublisher(), book.getPublisher());
        Assertions.assertEquals(bookEditRequestDTO.getTotalQuantity(), book.getTotalQuantity());
        Assertions.assertEquals(oldAvailableQuantity + (bookEditRequestDTO.getTotalQuantity() - oldTotalQuantity),
                book.getAvailableQuantity());

        Assertions.assertEquals(book.getTitle(), response.getTitle());
        Assertions.assertEquals(book.getPublisher(), response.getPublisher());
        Assertions.assertEquals(book.getIsbn(), response.getIsbn());
        Assertions.assertEquals(book.getYear(), response.getYear());
        Assertions.assertEquals(book.getIsActive(), response.getIsActive());
        Assertions.assertEquals(book.getDeactivationReason(), response.getDeactivationReason());
        Assertions.assertEquals(book.getAvailableQuantity(), response.getAvailableQuantity());
        Assertions.assertEquals(book.getTotalQuantity(), response.getTotalQuantity());
        Assertions.assertEquals(book.getGenres().size(), response.getGenres().size());
        Assertions.assertEquals(book.getAuthors().size(), response.getAuthors().size());
        Assertions.assertTrue(book.getAuthors().stream().map(a -> a.getFirstName() + a.getLastName()).toList()
                .containsAll(response.getAuthors().stream().map(a -> a.getFirstName() + a.getLastName()).toList()));
        Assertions.assertTrue(book.getGenres().stream().map(Genre::getName).toList()
                .containsAll(response.getGenres().stream().map(GenreDTO::getName).toList()));
    }

    @Test
    @Transactional
    void editBook_DecreaseQuantity_Success() throws Exception{
        Book book = genBook(1);
        User user = genAdmin();
        BookEditRequestDTO bookEditRequestDTO = new BookEditRequestDTO();
        bookEditRequestDTO.setTitle("edit book test title");
        bookEditRequestDTO.setPublisher("edit book test publisher");
        bookEditRequestDTO.setTotalQuantity(3);
        int oldAvailableQuantity = book.getAvailableQuantity();
        int oldTotalQuantity = book.getTotalQuantity();

        MockHttpServletResponse result = this.mockMvc.perform(put("/books/edit/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookEditRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        BookResponseDTO response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);

        Assertions.assertEquals(bookEditRequestDTO.getTitle(), book.getTitle());
        Assertions.assertEquals(bookEditRequestDTO.getPublisher(), book.getPublisher());
        Assertions.assertEquals(bookEditRequestDTO.getTotalQuantity(), book.getTotalQuantity());
        Assertions.assertEquals(oldAvailableQuantity - (oldTotalQuantity - bookEditRequestDTO.getTotalQuantity()),
                book.getAvailableQuantity());

        Assertions.assertEquals(book.getTitle(), response.getTitle());
        Assertions.assertEquals(book.getPublisher(), response.getPublisher());
        Assertions.assertEquals(book.getIsbn(), response.getIsbn());
        Assertions.assertEquals(book.getYear(), response.getYear());
        Assertions.assertEquals(book.getIsActive(), response.getIsActive());
        Assertions.assertEquals(book.getDeactivationReason(), response.getDeactivationReason());
        Assertions.assertEquals(book.getAvailableQuantity(), response.getAvailableQuantity());
        Assertions.assertEquals(book.getTotalQuantity(), response.getTotalQuantity());
        Assertions.assertEquals(book.getGenres().size(), response.getGenres().size());
        Assertions.assertEquals(book.getAuthors().size(), response.getAuthors().size());
        Assertions.assertTrue(book.getAuthors().stream().map(a -> a.getFirstName() + a.getLastName()).toList()
                .containsAll(response.getAuthors().stream().map(a -> a.getFirstName() + a.getLastName()).toList()));
        Assertions.assertTrue(book.getGenres().stream().map(Genre::getName).toList()
                .containsAll(response.getGenres().stream().map(GenreDTO::getName).toList()));
    }

    @Test
    @Transactional
    void editBook_ValidationError() throws Exception{
        Book book = genBook(1);
        User user = genAdmin();
        BookEditRequestDTO bookEditRequestDTO = new BookEditRequestDTO();
        bookEditRequestDTO.setTitle("");
        bookEditRequestDTO.setPublisher("");
        bookEditRequestDTO.setTotalQuantity(-5);

        MockHttpServletResponse result = this.mockMvc.perform(put("/books/edit/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookEditRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertTrue(bookEditValidationMessages.containsAll(response.getMessages()));
    }

    @Test
    @Transactional
    void editBook_ForbiddenWhenNotAdmin() throws Exception{
        Book book = genBook(1);
        User user = genUser();
        BookEditRequestDTO bookEditRequestDTO = new BookEditRequestDTO();
        bookEditRequestDTO.setTitle("edit book test title");
        bookEditRequestDTO.setPublisher("edit book test publisher");
        bookEditRequestDTO.setTotalQuantity(3);

        MockHttpServletResponse result = this.mockMvc.perform(put("/books/edit/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookEditRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @Transactional
    void editBook_BookNotFoundException() throws Exception{
        genBook(1);
        User user = genAdmin();
        BookEditRequestDTO bookEditRequestDTO = new BookEditRequestDTO();
        bookEditRequestDTO.setTitle("edit book test title");
        bookEditRequestDTO.setPublisher("edit book test publisher");
        bookEditRequestDTO.setTotalQuantity(3);

        MockHttpServletResponse result = this.mockMvc.perform(put("/books/edit/" + -1)
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookEditRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.BOOK_NOT_FOUND, -1), response.getMessages().get(0));
    }

    @Test
    @Transactional
    void editBook_InsufficientTotalQuantityException() throws Exception{
        Book book = genBook(1);
        book.setAvailableQuantity(1);
        User user = genAdmin();
        BookEditRequestDTO bookEditRequestDTO = new BookEditRequestDTO();
        bookEditRequestDTO.setTitle("edit book test title");
        bookEditRequestDTO.setPublisher("edit book test publisher");
        bookEditRequestDTO.setTotalQuantity(2);

        MockHttpServletResponse result = this.mockMvc.perform(put("/books/edit/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookEditRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Constants.INSUFFICIENT_BOOK_TOTAL_QUANTITY, response.getMessages().get(0));
    }

    //rentBook
    @Test
    @Transactional
    void rentBook_AsUser_Success() throws Exception {
        User user = genRentUser();
        Book book = genBook(1);
        int oldQuantity = book.getAvailableQuantity();

        MockHttpServletResponse result = this.mockMvc.perform(post("/books/rent/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse();

        RentResponseDTO response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });
        Rent savedRent = rentRepository.findByBookIdAndUserId(book.getId(), user.getId()).orElse(null);

        Assertions.assertNotNull(savedRent);
        Assertions.assertNotNull(response);

        Assertions.assertEquals(book, savedRent.getBook());
        Assertions.assertEquals(user, savedRent.getUser());
        Assertions.assertEquals(book.getId(), savedRent.getBook().getId());
        Assertions.assertEquals(user.getId(), savedRent.getUser().getId());
        Assertions.assertTrue(user.getRents().contains(savedRent));
        Assertions.assertEquals(LocalDate.now(), savedRent.getRentDate());
        Assertions.assertNull(savedRent.getReturnDate());
        Assertions.assertEquals(LocalDate.now().plusDays(30), savedRent.getExpectedReturnDate());
        Assertions.assertEquals(oldQuantity - 1, book.getAvailableQuantity());

        Assertions.assertEquals(book.getTitle(), response.getBookTitle());
        Assertions.assertEquals(user.getUsername(), response.getRentedBy());
        Assertions.assertEquals(LocalDate.now(), response.getRentDate());
        Assertions.assertNull(response.getReturnDate());
        Assertions.assertEquals(LocalDate.now().plusDays(30), response.getExpectedReturnDate());
    }

    @Test
    @Transactional
    void rentBook_AsAdmin_Success() throws Exception {
        User user = genAdmin();
        User rentUser = genRentUser();
        Book book = genBook(1);
        int oldQuantity = book.getAvailableQuantity();
        RentCreateDTO rentCreateDTO = new RentCreateDTO();
        rentCreateDTO.setUserId(rentUser.getId());

        MockHttpServletResponse result = this.mockMvc.perform(post("/books/rent/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rentCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse();

        RentResponseDTO response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });
        Rent savedRent = rentRepository.findByBookIdAndUserId(book.getId(), rentUser.getId()).orElse(null);

        Assertions.assertNotNull(savedRent);
        Assertions.assertNotNull(response);

        Assertions.assertEquals(book, savedRent.getBook());
        Assertions.assertEquals(rentUser, savedRent.getUser());
        Assertions.assertEquals(book.getId(), savedRent.getBook().getId());
        Assertions.assertEquals(rentUser.getId(), savedRent.getUser().getId());
        Assertions.assertTrue(rentUser.getRents().contains(savedRent));
        Assertions.assertEquals(LocalDate.now(), savedRent.getRentDate());
        Assertions.assertNull(savedRent.getReturnDate());
        Assertions.assertEquals(LocalDate.now().plusDays(30), savedRent.getExpectedReturnDate());
        Assertions.assertEquals(oldQuantity - 1, book.getAvailableQuantity());

        Assertions.assertEquals(book.getTitle(), response.getBookTitle());
        Assertions.assertEquals(rentUser.getUsername(), response.getRentedBy());
        Assertions.assertEquals(LocalDate.now(), response.getRentDate());
        Assertions.assertNull(response.getReturnDate());
        Assertions.assertEquals(LocalDate.now().plusDays(30), response.getExpectedReturnDate());
    }

    @Test
    @Transactional
    void rentBook_AsBronzeUser_Success() throws Exception {
        User user = genBronzeUser();
        Book book = genBook(1);
        int oldQuantity = book.getAvailableQuantity();

        MockHttpServletResponse result = this.mockMvc.perform(post("/books/rent/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse();

        RentResponseDTO response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });
        Rent savedRent = rentRepository.findByBookIdAndUserId(book.getId(), user.getId()).orElse(null);

        Assertions.assertNotNull(savedRent);
        Assertions.assertNotNull(response);

        Assertions.assertEquals(book, savedRent.getBook());
        Assertions.assertEquals(user, savedRent.getUser());
        Assertions.assertEquals(book.getId(), savedRent.getBook().getId());
        Assertions.assertEquals(user.getId(), savedRent.getUser().getId());
        Assertions.assertTrue(user.getRents().contains(savedRent));
        Assertions.assertEquals(LocalDate.now(), savedRent.getRentDate());
        Assertions.assertNull(savedRent.getReturnDate());
        Assertions.assertEquals(LocalDate.now().plusDays(30), savedRent.getExpectedReturnDate());
        Assertions.assertEquals(oldQuantity - 1, book.getAvailableQuantity());

        Assertions.assertEquals(book.getTitle(), response.getBookTitle());
        Assertions.assertEquals(user.getUsername(), response.getRentedBy());
        Assertions.assertEquals(LocalDate.now(), response.getRentDate());
        Assertions.assertNull(response.getReturnDate());
        Assertions.assertEquals(LocalDate.now().plusDays(30), response.getExpectedReturnDate());
    }

    @Test
    @Transactional
    void rentBook_AsSilverUser_Success() throws Exception {
        User user = genSilverUser();
        Book book = genBook(1);
        int oldQuantity = book.getAvailableQuantity();

        MockHttpServletResponse result = this.mockMvc.perform(post("/books/rent/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse();

        RentResponseDTO response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });
        Rent savedRent = rentRepository.findByBookIdAndUserId(book.getId(), user.getId()).orElse(null);

        Assertions.assertNotNull(savedRent);
        Assertions.assertNotNull(response);

        Assertions.assertEquals(book, savedRent.getBook());
        Assertions.assertEquals(user, savedRent.getUser());
        Assertions.assertEquals(book.getId(), savedRent.getBook().getId());
        Assertions.assertEquals(user.getId(), savedRent.getUser().getId());
        Assertions.assertTrue(user.getRents().contains(savedRent));
        Assertions.assertEquals(LocalDate.now(), savedRent.getRentDate());
        Assertions.assertNull(savedRent.getReturnDate());
        Assertions.assertEquals(LocalDate.now().plusDays(45), savedRent.getExpectedReturnDate());
        Assertions.assertEquals(oldQuantity - 1, book.getAvailableQuantity());

        Assertions.assertEquals(book.getTitle(), response.getBookTitle());
        Assertions.assertEquals(user.getUsername(), response.getRentedBy());
        Assertions.assertEquals(LocalDate.now(), response.getRentDate());
        Assertions.assertNull(response.getReturnDate());
        Assertions.assertEquals(LocalDate.now().plusDays(45), response.getExpectedReturnDate());
    }

    @Test
    @Transactional
    void rentBook_AsGoldUser_Success() throws Exception {
        User user = genGoldUser();
        Book book = genBook(1);
        int oldQuantity = book.getAvailableQuantity();

        MockHttpServletResponse result = this.mockMvc.perform(post("/books/rent/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse();

        RentResponseDTO response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });
        Rent savedRent = rentRepository.findByBookIdAndUserId(book.getId(), user.getId()).orElse(null);

        Assertions.assertNotNull(savedRent);
        Assertions.assertNotNull(response);

        Assertions.assertEquals(book, savedRent.getBook());
        Assertions.assertEquals(user, savedRent.getUser());
        Assertions.assertEquals(book.getId(), savedRent.getBook().getId());
        Assertions.assertEquals(user.getId(), savedRent.getUser().getId());
        Assertions.assertTrue(user.getRents().contains(savedRent));
        Assertions.assertEquals(LocalDate.now(), savedRent.getRentDate());
        Assertions.assertNull(savedRent.getReturnDate());
        Assertions.assertEquals(LocalDate.now().plusDays(60), savedRent.getExpectedReturnDate());
        Assertions.assertEquals(oldQuantity - 1, book.getAvailableQuantity());

        Assertions.assertEquals(book.getTitle(), response.getBookTitle());
        Assertions.assertEquals(user.getUsername(), response.getRentedBy());
        Assertions.assertEquals(LocalDate.now(), response.getRentDate());
        Assertions.assertNull(response.getReturnDate());
        Assertions.assertEquals(LocalDate.now().plusDays(60), response.getExpectedReturnDate());
    }

    @Test
    @Transactional
    void rentBook_ForbiddenWhenNotPermittedRole() throws Exception {
        User user = genRentUser();
        Book book = genBook(1);

        MockHttpServletResponse result = this.mockMvc.perform(post("/books/rent/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority("TEST"))))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @Transactional
    void rentBook_BookNotFoundException() throws Exception{
        User user = genRentUser();
        genBook(1);

        MockHttpServletResponse result = this.mockMvc.perform(post("/books/rent/" + -5)
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.BOOK_NOT_FOUND, -5), response.getMessages().get(0));
    }

    @Test
    @Transactional
    void rentBook_BookNotActiveException() throws Exception {
        User user = genRentUser();
        Book book = genBook0(1);

        MockHttpServletResponse result = this.mockMvc.perform(post("/books/rent/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.BOOK_NOT_ACTIVE, book.getId()), response.getMessages().get(0));
    }

    @Test
    @Transactional
    void rentBook_InsufficientAvailableQuantityException() throws Exception {
        User user = genRentUser();
        Book book = genBook(1);
        book.setAvailableQuantity(0);

        MockHttpServletResponse result = this.mockMvc.perform(post("/books/rent/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Constants.INSUFFICIENT_BOOK_AVAILABLE_QUANTITY, response.getMessages().get(0));
    }

    @Test
    @Transactional
    void rentBook_AsAdmin_UserNotFoundException() throws Exception{
        User user = genAdmin();
        Book book = genBook(1);
        RentCreateDTO rentCreateDTO = new RentCreateDTO();
        rentCreateDTO.setUserId(-10);

        MockHttpServletResponse result = this.mockMvc.perform(post("/books/rent/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rentCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.USER_NOT_FOUND, rentCreateDTO.getUserId()), response.getMessages().get(0));
    }

    @Test
    @Transactional
    void rentBook_AsUser_NotEligibleToRentException() throws Exception{
        User user = genUser();
        User rentUser = genRentUser();
        Book book = genBook(1);
        RentCreateDTO rentCreateDTO = new RentCreateDTO();
        rentCreateDTO.setUserId(rentUser.getId());

        MockHttpServletResponse result = this.mockMvc.perform(post("/books/rent/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rentCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.USER_NOT_ELIGIBLE_TO_RENT, user.getUsername(), rentUser.getUsername()),
                response.getMessages().get(0));
    }

    @Test
    @Transactional
    void rentBook_UserProlongedRentsException() throws Exception {
        User user = genGoldUser();
        user.setHasProlongedRents(true);
        Book book = genBook(1);

        MockHttpServletResponse result = this.mockMvc.perform(post("/books/rent/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.USER_PROLONGED_RENTS, user.getUsername()),
                response.getMessages().get(0));
    }

    @Test
    @Transactional
    void rentBook_DuplicateException() throws Exception {
        User user = genRentUser();
        Book book = genBook(1);
        genRent(user, book);

        MockHttpServletResponse result = this.mockMvc.perform(post("/books/rent/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Constants.DUPLICATE_RENT, response.getMessages().get(0));
    }

    @Test
    @Transactional
    void rentBook_RentCapException() throws Exception {
        User user = genRentUser();
        Book book1 = genBook(1);
        Book book2 = genBook(2);
        Book book3 = genBook(3);
        genRent(user, book1);
        genRent(user, book2);
        genRent(user, book3);
        Book book = genBook(4);

        MockHttpServletResponse result = this.mockMvc.perform(post("/books/rent/" + book.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.RENT_CAP_MSG, user.getUsername()), response.getMessages().get(0));
    }

    //returnBook
    @Test
    @Transactional
    void returnBook_Success() throws Exception {
        User user = genUser();
        Book book = genBook(1);
        int oldQuantity = book.getAvailableQuantity();
        Rent rent = genRent(user, book);

        MockHttpServletResponse result = this.mockMvc.perform(put("/rents/return/" + rent.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        RentResponseDTO response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(LocalDate.now(), rent.getReturnDate());
        Assertions.assertEquals(oldQuantity + 1, book.getAvailableQuantity());
    }

    @Test
    @Transactional
    void returnBook_ForbiddenWhenNotAuthorizedCorrectly() throws Exception {
        User user = genUser();
        User user1 = genRentUser();
        Book book = genBook(1);
        Rent rent = genRent(user, book);

        MockHttpServletResponse result = this.mockMvc.perform(put("/rents/return/" + rent.getId())
                        .with(user(user1.getUsername()).authorities(new SimpleGrantedAuthority("TEST"))))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @Transactional
    void returnBook_BookAlreadyReturnedException() throws Exception {
        User user = genUser();
        Book book = genBook(1);
        Rent rent = genRent(user, book);
        rent.setReturnDate(LocalDate.now().plusDays(3));

        MockHttpServletResponse result = this.mockMvc.perform(put("/rents/return/" + rent.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Constants.BOOK_ALREADY_RETURNED, response.getMessages().get(0));
    }

    @Test
    @Transactional
    void returnBook_InsufficientBalanceException() throws Exception {
        User user = genUser();
        user.setBalance(BigDecimal.valueOf(-10));
        Book book = genBook(1);
        Rent rent = genRent(user, book);

        MockHttpServletResponse result = this.mockMvc.perform(put("/rents/return/" + rent.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.INSUFFICIENT_BALANCE, user.getId()),
                response.getMessages().get(0));
    }

    //xmlImport
    @Nested
    class XmlImportSuccess extends BaseTest{
        @BeforeEach
        void init() throws IOException {
            Path zip1From = Paths.get("src/test/resources/success-test1.zip");
            Path zip1To = Paths.get("src/main/resources/zips-to-import/success-test1.zip");
            Path zip2From = Paths.get("src/test/resources/success-test2.zip");
            Path zip2To = Paths.get("src/main/resources/zips-to-import/success-test2.zip");
            Files.copy(zip1From, zip1To, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(zip2From, zip2To, StandardCopyOption.REPLACE_EXISTING);
        }

        @AfterEach
        void cleanUp() throws IOException {
            Path zip1 = Paths.get("src/main/resources/imported-zips/success-test1.zip");
            Path zip2 = Paths.get("src/main/resources/imported-zips/success-test2.zip");
            Files.delete(zip1);
            Files.delete(zip2);
        }

        @Test
        @Transactional
        void xmlImport_Success() throws Exception {
            User user = genAdmin();
            String expectedIsbn1 = "1-123-123-123-123";
            String expectedIsbn2 = "2-456-456-456-456";
            String expectedIsbn3 = "3-789-789-789-789";
            Assertions.assertNull(bookRepository.findByIsbn(expectedIsbn1).orElse(null));
            Assertions.assertNull(bookRepository.findByIsbn(expectedIsbn2).orElse(null));
            Assertions.assertNull(bookRepository.findByIsbn(expectedIsbn3).orElse(null));

            MockHttpServletResponse result = this.mockMvc.perform(post("/books/xml-import")
                            .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString()))))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andReturn().getResponse();

            String response = result.getContentAsString();

            Book book1 = bookRepository.findByIsbn(expectedIsbn1).orElse(null);
            Book book2 = bookRepository.findByIsbn(expectedIsbn2).orElse(null);
            Book book3 = bookRepository.findByIsbn(expectedIsbn3).orElse(null);
            BookXMLImportAudit bookXMLImportAudit1 = bookXMLImportAuditRepository.findLastAuditByZipName(
                    "success-test1.zip").orElse(null);
            BookXMLImportAudit bookXMLImportAudit2 = bookXMLImportAuditRepository.findLastAuditByZipName(
                    "success-test2.zip").orElse(null);

            Assertions.assertNotNull(response);
            Assertions.assertEquals("Successfully imported 3 books", response);

            Assertions.assertNotNull(book1);
            Assertions.assertNotNull(book2);
            Assertions.assertNotNull(book3);
            Assertions.assertNotNull(bookXMLImportAudit1);
            Assertions.assertNotNull(bookXMLImportAudit2);
        }
    }

    @Nested
    class XmlImportValidationException extends BaseTest{
        @BeforeEach
        void init() throws IOException {
            Path zip1From = Paths.get("src/test/resources/validations.zip");
            Path zip1To = Paths.get("src/main/resources/zips-to-import/validations.zip");
            Files.copy(zip1From, zip1To, StandardCopyOption.REPLACE_EXISTING);
        }

        @AfterEach
        void cleanUp() throws IOException {
            Path zip1 = Paths.get("src/main/resources/zips-to-import/validations.zip");
            Files.delete(zip1);
        }

        @Test
        @Transactional
        void xmlImport_ValidationException() throws Exception {
            User user = genAdmin();

            MockHttpServletResponse result = this.mockMvc.perform(post("/books/xml-import")
                            .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString()))))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andReturn().getResponse();

            BookXMLImportAudit bookXMLImportAudit1 = bookXMLImportAuditRepository.findLastAuditByZipNameAndXmlName(
                    "validations.zip", "book1.xml")
                    .orElse(null);
            BookXMLImportAudit bookXMLImportAudit2 = bookXMLImportAuditRepository.findLastAuditByZipNameAndXmlName(
                    "validations.zip", "book2.xml")
                    .orElse(null);

            String response = result.getContentAsString();

            Assertions.assertNotNull(response);
            Assertions.assertEquals("Fail", response);

            Assertions.assertNotNull(bookXMLImportAudit1);
            Assertions.assertNotNull(bookXMLImportAudit2);
            Assertions.assertEquals(Constants.DUPLICATE_ISBN, bookXMLImportAudit1.getMessage());
            Assertions.assertEquals(Constants.XML_VALIDATION_MSG, bookXMLImportAudit2.getMessage());
        }
    }

    @Nested
    class XmlImportInvalidFileException extends BaseTest{
        @BeforeEach
        void init() throws IOException {
            Path zip1From = Paths.get("src/test/resources/success-test1.zip");
            Path zip1To = Paths.get("src/main/resources/zips-to-import/success-test1.zip");
            Path zip2From = Paths.get("src/test/resources/fail.txt");
            Path zip2To = Paths.get("src/main/resources/zips-to-import/fail.txt");
            Files.copy(zip1From, zip1To, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(zip2From, zip2To, StandardCopyOption.REPLACE_EXISTING);
        }

        @AfterEach
        void cleanUp() throws IOException {
            Path zip1 = Paths.get("src/main/resources/zips-to-import/success-test1.zip");
            Path zip2 = Paths.get("src/main/resources/zips-to-import/fail.txt");
            Files.delete(zip1);
            Files.delete(zip2);
        }

        @Test
        @Transactional
        void xmlImport_InvalidFileException() throws Exception {
            User user = genAdmin();

            MockHttpServletResponse result = this.mockMvc.perform(post("/books/xml-import")
                            .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString()))))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andReturn().getResponse();

            ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
            });

            Assertions.assertNotNull(response);
            Assertions.assertEquals(Constants.INVALID_FILE, response.getMessages().get(0));
        }
    }

    @Nested
    class XmlImportInvalidZipException extends BaseTest{
        @BeforeEach
        void init() throws IOException {
            Path zip1From = Paths.get("src/test/resources/invalid-zip.zip");
            Path zip1To = Paths.get("src/main/resources/zips-to-import/invalid-zip.zip");
            Files.copy(zip1From, zip1To, StandardCopyOption.REPLACE_EXISTING);
        }

        @AfterEach
        void cleanUp() throws IOException {
            Path zip1 = Paths.get("src/main/resources/zips-to-import/invalid-zip.zip");
            Files.delete(zip1);
        }

        @Test
        @Transactional
        void xmlImport_InvalidZipException() throws Exception {
            User user = genAdmin();

            MockHttpServletResponse result = this.mockMvc.perform(post("/books/xml-import")
                            .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString()))))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andReturn().getResponse();

            ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
            });

            Assertions.assertNotNull(response);
            Assertions.assertEquals(Constants.INVALID_ZIP, response.getMessages().get(0));
        }
    }

    @Test
    @Transactional
    void simultaneous_editBook() throws Exception {
        User user = genAdmin();
        BookEditRequestDTO request1 = new BookEditRequestDTO();
        request1.setPublisher("qwerty");
        request1.setTitle("asdfg");
        request1.setTotalQuantity(10);
        BookEditRequestDTO request2 = new BookEditRequestDTO();
        request2.setPublisher("zxc");
        request2.setTitle("asdfg");
        request2.setTotalQuantity(10);
        Book book = bookRepository.findById(710).orElse(null);

        Runnable r1 = () -> {
            try {
                MockHttpServletResponse result = this.mockMvc.perform(put("/books/edit/" + book.getId())
                                .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString())))
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request1))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println(book.getPublisher());
        };

        Runnable r2 = () -> {
            try {
                MockHttpServletResponse result = this.mockMvc.perform(put("/books/edit/" + book.getId())
                                .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString())))
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request2))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println(book.getPublisher());
        };

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        Assertions.assertEquals("zxc", book.getPublisher());
        Assertions.assertEquals("qwerty", book.getPublisher());
    }
}
