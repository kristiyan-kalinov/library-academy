package com.kodar.academy.Library.controller;

import com.kodar.academy.Library.model.dto.book.*;
import com.kodar.academy.Library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
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
    public ResponseEntity<String> deleteBookById(@PathVariable("id") int id) throws Exception {
        bookService.deleteBook(id);
        return new ResponseEntity<>("Book successfully deleted", HttpStatus.OK);
    }

    @PostMapping("/books")
    public ResponseEntity<BookResponseDTO> addBook(@Valid @RequestBody BookCreateDTO bookCreateDTO) {
        BookResponseDTO bookResponseDTO = bookService.addBook(bookCreateDTO);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/books/edit/{id}")
    public ResponseEntity<BookResponseDTO> editBook(@PathVariable("id") int id,
                                                    @Valid @RequestBody BookEditRequestDTO bookEditRequestDTO) {
        BookResponseDTO bookResponseDTO = bookService.editBook(id, bookEditRequestDTO);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.OK);

    }

    @PutMapping("/books/change-status/{id}")
    public ResponseEntity<BookResponseDTO> changeStatus(@PathVariable("id") int id,
                                                        @RequestBody BookChangeStatusDTO bookChangeStatusDTO) {
        BookResponseDTO bookResponseDTO = bookService.changeStatus(id, bookChangeStatusDTO);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.OK);
    }


}
