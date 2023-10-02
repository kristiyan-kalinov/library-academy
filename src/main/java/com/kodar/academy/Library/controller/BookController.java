package com.kodar.academy.Library.controller;

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
        List<BookResponseDTO> books = bookService.getAllBooks(bookFilterRequest);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable("id") int id) throws Exception {
        BookResponseDTO bookResponseDTO = bookService.getBookById(id);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/books/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteBookById(@PathVariable("id") int id) throws Exception {
        bookService.deleteBook(id);
        return new ResponseEntity<>("Book successfully deleted", HttpStatus.OK);
    }

    @PostMapping("/books")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BookResponseDTO> addBook(@Valid @RequestBody BookCreateDTO bookCreateDTO) {
        BookResponseDTO bookResponseDTO = bookService.addBook(bookCreateDTO);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/books/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BookResponseDTO> editBook(@PathVariable("id") int id,
                                                    @Valid @RequestBody BookEditRequestDTO bookEditRequestDTO) throws Exception {
        BookResponseDTO bookResponseDTO = bookService.editBook(id, bookEditRequestDTO);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.OK);

    }

    @PutMapping("/books/change-status/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BookResponseDTO> changeStatus(@PathVariable("id") int id,
                                                        @RequestBody BookChangeStatusDTO bookChangeStatusDTO) {
        BookResponseDTO bookResponseDTO = bookService.changeStatus(id, bookChangeStatusDTO);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/books/rent/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<RentResponseDTO> rentBook(@PathVariable("id") int id,
                                                    @RequestBody @Nullable RentCreateDTO rentCreateDTO) throws Exception {
        RentResponseDTO rentResponseDTO = rentService.createRent(id, rentCreateDTO);
        return new ResponseEntity<>(rentResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/rents/return/{id}")
    @PreAuthorize("@rentService.checkAuth(#id) == authentication.name")
    public ResponseEntity<RentResponseDTO> returnBook(@PathVariable("id") int id) throws Exception {
        RentResponseDTO rentResponseDTO = rentService.returnRent(id);
        return new ResponseEntity<>(rentResponseDTO, HttpStatus.OK);
    }

}
