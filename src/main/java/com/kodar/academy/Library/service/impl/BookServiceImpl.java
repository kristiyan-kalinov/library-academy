package com.kodar.academy.Library.service.impl;

import com.kodar.academy.Library.model.dto.book.BookCreateDTO;
import com.kodar.academy.Library.model.dto.book.BookResponseDTO;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.mapper.BookMapper;
import com.kodar.academy.Library.repository.BookRepository;
import com.kodar.academy.Library.service.BookService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public List<BookResponseDTO> getAllBooks() {
        List<BookResponseDTO> books = new ArrayList<>();
        bookRepository.findAll().stream()
                .map(BookMapper::mapToResponse)
                .forEach(books::add);
        return books;
    }

    @Override
    public BookResponseDTO getBookById(int id) {
        Optional<Book> bookData = bookRepository.findById(id);
        Book book = new Book();
        if(bookData.isPresent()) {
            book = bookData.get();
        }
        return BookMapper.mapToResponse(book);
    }

    @Override
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BookResponseDTO addBook(BookCreateDTO bookCreateDTO) {
        Book book = bookMapper.mapToBook(bookCreateDTO);
        bookRepository.save(book);
        return BookMapper.mapToResponse(book);
    }

}
