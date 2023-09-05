package com.kodar.academy.Library.model.mapper;

import com.kodar.academy.Library.model.dto.author.AuthorDTO;
import com.kodar.academy.Library.model.dto.book.BookCreateDTO;
import com.kodar.academy.Library.model.dto.book.BookResponseDTO;
import com.kodar.academy.Library.model.entity.Author;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.repository.AuthorRepository;
import com.kodar.academy.Library.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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

    public Book mapToBook(BookCreateDTO source) {
        Book target = new Book();
        target.setIsbn(source.getIsbn());
        target.setTitle(source.getTitle());
        target.setYear(source.getYear());
        target.setPublisher(source.getPublisher());
        if(source.getDateAdded() != null) {
            target.setDateAdded(source.getDateAdded());
        }
        else {
            target.setDateAdded(LocalDateTime.now());
        }
        target.setGenres(source.getGenres().stream()
                .map(genre -> genreRepository.findByName(genre.getName()).orElse(null))
                .collect(Collectors.toSet()));
        target.setAuthors(source.getAuthors().stream()
                .map(author -> {
                    addAuthor(author);
                    return authorRepository.findByFirstNameAndLastName(author.getFirstName(), author.getLastName()).orElse(null);
                })
                .collect(Collectors.toSet()));
        return target;
    }

    private void addAuthor(AuthorDTO authorDTO) {
        if(authorRepository.findByFirstNameAndLastName(authorDTO.getFirstName(), authorDTO.getLastName()).isEmpty()) {
            Author author = new Author();
            author.setFirstName(authorDTO.getFirstName());
            author.setLastName(authorDTO.getLastName());
            authorRepository.save(author);
        };
    }

}
