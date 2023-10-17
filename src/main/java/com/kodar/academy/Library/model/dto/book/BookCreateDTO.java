package com.kodar.academy.Library.model.dto.book;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.dto.author.AuthorDTO;
import com.kodar.academy.Library.model.dto.genre.GenreDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class BookCreateDTO {

    @NotBlank(message = Constants.ISBN_REQUIRED)
//    @Pattern(regexp = "^(?=(?:\\D*\\d){13}\\D*$)\\d{1,5}[-\\s]?\\d{1,7}[-\\s]?\\d{1,6}[-\\s]?\\d{1}$\n",
//            message = "Invalid ISBN format")
    private String isbn;
    @NotBlank(message = Constants.TITLE_REQUIRED)
    @Size(min = 1, max = 255, message = Constants.TITLE_LENGTH)
    private String title;
    @NotBlank(message = Constants.PUBLISHER_REQUIRED)
    @Size(min = 2, max = 64, message = Constants.PUBLISHER_LENGTH)
    private String publisher;
    private short year;
    private Set<GenreDTO> genres;
    private Set<@Valid AuthorDTO> authors;
    @Min(value = 0, message = Constants.TOTAL_QUANTITY_MIN_VALUE)
    private int totalQuantity;

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

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    @Override
    public String toString() {
        return "{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", year=" + year +
                ", genres=" + genres +
                ", authors=" + authors +
                ", totalQuantity=" + totalQuantity +
                '}';
    }
}
