package com.kodar.academy.Library.model.dto.book;

import com.kodar.academy.Library.model.dto.author.AuthorDTO;
import com.kodar.academy.Library.model.dto.genre.GenreDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class BookCreateDTO {

    @NotBlank(message = "ISBN is required")
//    @Pattern(regexp = "^(?=(?:\\D*\\d){13}\\D*$)\\d{1,5}[-\\s]?\\d{1,7}[-\\s]?\\d{1,6}[-\\s]?\\d{1}$\n",
//            message = "Invalid ISBN format")
    private String isbn;
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;
    @NotBlank(message = "Publisher is required")
    @Size(min = 2, max = 64, message = "Publisher must be between 2 and 64 characters")
    private String publisher;
    private short year;
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
}
