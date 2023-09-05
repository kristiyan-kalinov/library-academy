package com.kodar.academy.Library.model.dto.book;

import com.kodar.academy.Library.model.dto.author.AuthorDTO;
import com.kodar.academy.Library.model.dto.genre.GenreDTO;

import java.util.Set;

public class BookResponseDTO {

    private String isbn;
    private String title;
    private short year;
    private String publisher;
    private Set<AuthorDTO> authors;
    private Set<GenreDTO> genres;

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

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Set<AuthorDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorDTO> authors) {
        this.authors = authors;
    }

    public Set<GenreDTO> getGenres() {
        return genres;
    }

    public void setGenres(Set<GenreDTO> genres) {
        this.genres = genres;
    }
}
