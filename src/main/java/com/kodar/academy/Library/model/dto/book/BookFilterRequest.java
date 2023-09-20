package com.kodar.academy.Library.model.dto.book;

import java.util.Arrays;

public class BookFilterRequest {

    private String title;
    private String publisher;
    private Short yearBefore;
    private Short yearAfter;
    private int[] genres;
    private String[] authorFirstName;
    private String[] authorLastName;

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

    public Short getYearBefore() {
        return yearBefore;
    }

    public void setYearBefore(Short yearBefore) {
        this.yearBefore = yearBefore;
    }

    public Short getYearAfter() {
        return yearAfter;
    }

    public void setYearAfter(Short yearAfter) {
        this.yearAfter = yearAfter;
    }

    public int[] getGenres() {
        return genres;
    }

    public void setGenres(int[] genres) {
        this.genres = genres;
    }

    public String[] getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String[] authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String[] getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String[] authorLastName) {
        this.authorLastName = authorLastName;
    }

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", yearBefore=" + yearBefore +
                ", yearAfter=" + yearAfter +
                ", genres=" + Arrays.toString(genres) +
                ", authorFirstName=" + Arrays.toString(authorFirstName) +
                ", authorLastName=" + Arrays.toString(authorLastName) +
                '}';
    }
}
