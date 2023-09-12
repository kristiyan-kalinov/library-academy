package com.kodar.academy.Library.model.mapper;

import com.kodar.academy.Library.model.dto.book.BookCreateDTO;
import com.kodar.academy.Library.model.dto.book.BookResponseDTO;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.repository.AuthorRepository;
import com.kodar.academy.Library.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BookMapper {

    GenreRepository genreRepository;
    AuthorRepository authorRepository;

    @Autowired
    public BookMapper(GenreRepository genreRepository, AuthorRepository authorRepository) {
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
    }

    public static BookResponseDTO mapToResponse(Book source) {
        BookResponseDTO target = new BookResponseDTO();
        target.setIsbn(source.getIsbn());
        target.setTitle(source.getTitle());
        target.setYear(source.getYear());
        target.setPublisher(source.getPublisher());
        target.setAuthors(source.getAuthors().stream()
                .map(AuthorMapper::mapToResponse)
                .collect(Collectors.toSet()));
        target.setGenres(source.getGenres().stream()
                .map(GenreMapper::mapToResponse)
                .collect(Collectors.toSet()));
        return target;
    }

    public static Book mapToBook(BookCreateDTO source) {
        Book target = new Book();
        target.setIsbn(source.getIsbn());
        target.setTitle(source.getTitle());
        target.setYear(source.getYear());
        target.setPublisher(source.getPublisher());
        return target;
    }

}
