package com.kodar.academy.Library.controller;

import com.kodar.academy.Library.model.dto.book.BookCreateDTO;
import com.kodar.academy.Library.model.dto.book.BookEditRequestDTO;
import com.kodar.academy.Library.model.dto.book.BookResponseDTO;
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
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        List<BookResponseDTO> books = bookService.getAllBooks();

        if(books.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable("id") int id) {
        BookResponseDTO bookResponseDTO = bookService.getBookById(id);
        if(bookResponseDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/books/delete/{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable("id") int id) {
        if(bookService.getBookById(id) == null) {
            return new ResponseEntity<>("Book with that id doesn't exist", HttpStatus.BAD_REQUEST);
        }
        bookService.deleteBook(id);
        return new ResponseEntity<>("Book successfully deleted", HttpStatus.OK);
    }

    @PostMapping("/books")
    public ResponseEntity<BookResponseDTO> addBook(@Valid @RequestBody BookCreateDTO bookCreateDTO) {
        BookResponseDTO bookResponseDTO = bookService.addBook(bookCreateDTO);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/books/edit/{id}")
    public ResponseEntity<?> editBook(@PathVariable("id") int id,
                                                    @Valid @RequestBody BookEditRequestDTO bookEditRequestDTO) {

        if(bookService.getBookById(id) == null) {
            return new ResponseEntity<>("Book with that id doesn't exist", HttpStatus.BAD_REQUEST);
        }
        BookResponseDTO bookResponseDTO = bookService.editBook(id, bookEditRequestDTO);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.OK);

    }


}
