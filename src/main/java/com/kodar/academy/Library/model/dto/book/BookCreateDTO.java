package com.kodar.academy.Library.model.dto.book;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.dto.author.AuthorDTO;
import com.kodar.academy.Library.model.dto.genre.GenreDTO;
import com.kodar.academy.Library.model.validation.UniqueIsbn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

@JacksonXmlRootElement(localName = "book")
public class BookCreateDTO {

    @JacksonXmlProperty(localName = "isbn")
    @NotBlank(message = Constants.ISBN_REQUIRED)
    @UniqueIsbn
//    @Pattern(regexp = "^(?=(?:\\D*\\d){13}\\D*$)\\d{1,5}[-\\s]?\\d{1,7}[-\\s]?\\d{1,6}[-\\s]?\\d{1}$\n",
//            message = "Invalid ISBN format")
    private String isbn;
    @JacksonXmlProperty(localName = "title")
    @NotBlank(message = Constants.TITLE_REQUIRED)
    @Size(min = 1, max = 255, message = Constants.TITLE_LENGTH)
    private String title;
    @JacksonXmlProperty(localName = "publisher")
    @NotBlank(message = Constants.PUBLISHER_REQUIRED)
    @Size(min = 2, max = 64, message = Constants.PUBLISHER_LENGTH)
    private String publisher;
    @JacksonXmlProperty(localName = "year")
    private short year;
    @JacksonXmlProperty(localName = "genres")
    private Set<@Valid GenreDTO> genres;
    @JacksonXmlProperty(localName = "authors")
    private Set<@Valid AuthorDTO> authors;
    @JacksonXmlProperty(localName = "totalQuantity")
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
