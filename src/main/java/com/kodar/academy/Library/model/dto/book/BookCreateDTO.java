package com.kodar.academy.Library.model.dto.book;

import com.kodar.academy.Library.model.dto.author.AuthorDTO;
import com.kodar.academy.Library.model.dto.genre.GenreDTO;

import java.time.LocalDateTime;
import java.util.Set;

public class BookCreateDTO {

    private String isbn;
    private String title;
    private String publisher;
    private short year;
    private LocalDateTime dateAdded;
    private Set<GenreDTO> genres;
    private Set<AuthorDTO> authors;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public Set<GenreDTO> getGenres() {
        return genres;
    }

    public void setGenres(Set<GenreDTO> genres) {
        this.genres = genres;
    }

    public Set<AuthorDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorDTO> authors) {
        this.authors = authors;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }
}
