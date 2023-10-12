package com.kodar.academy.Library.controller;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.dto.book.*;
import com.kodar.academy.Library.model.dto.rent.RentCreateDTO;
import com.kodar.academy.Library.model.dto.rent.RentResponseDTO;
import com.kodar.academy.Library.service.BookService;
import com.kodar.academy.Library.service.RentService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    BookService bookService;
    RentService rentService;

    @Autowired
    public BookController(BookService bookService, RentService rentService) {

        this.bookService = bookService;
        this.rentService = rentService;
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookResponseDTO>> getAllBooks(BookFilterRequest bookFilterRequest) {
        return new ResponseEntity<>(bookService.getAllBooks(bookFilterRequest), HttpStatus.OK);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable("id") int id) {
        return new ResponseEntity<>(bookService.getBookById(id), HttpStatus.OK);
    }

    @DeleteMapping("/books/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteBookById(@PathVariable("id") int id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(Constants.SUCCESSFUL_BOOK_DELETE, HttpStatus.OK);
    }

    @PostMapping("/books")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BookResponseDTO> addBook(@Valid @RequestBody BookCreateDTO bookCreateDTO) {
        return new ResponseEntity<>(bookService.addBook(bookCreateDTO), HttpStatus.CREATED);
    }

    @PutMapping("/books/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BookResponseDTO> editBook(@PathVariable("id") int id,
                                                    @Valid @RequestBody BookEditRequestDTO bookEditRequestDTO) {
        return new ResponseEntity<>(bookService.editBook(id, bookEditRequestDTO), HttpStatus.OK);

    }

    @PutMapping("/books/change-status/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BookResponseDTO> changeStatus(@PathVariable("id") int id,
                                                        @RequestBody BookChangeStatusDTO bookChangeStatusDTO) {
        return new ResponseEntity<>(bookService.changeStatus(id, bookChangeStatusDTO), HttpStatus.OK);
    }

    @PostMapping("/books/rent/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<RentResponseDTO> rentBook(@PathVariable("id") int id,
                                                    @Valid @RequestBody @Nullable RentCreateDTO rentCreateDTO) {
        return new ResponseEntity<>(rentService.createRent(id, rentCreateDTO), HttpStatus.CREATED);
    }

    @PutMapping("/rents/return/{id}")
    @PreAuthorize("@rentService.checkAuth(#id) == authentication.name")
    public ResponseEntity<RentResponseDTO> returnBook(@PathVariable("id") int id) {
        return new ResponseEntity<>(rentService.returnRent(id), HttpStatus.OK);
    }

}
