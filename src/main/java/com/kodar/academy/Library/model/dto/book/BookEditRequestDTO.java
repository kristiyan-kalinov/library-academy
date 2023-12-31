package com.kodar.academy.Library.model.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BookEditRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "Publisher is required")
    @Size(min = 2, max = 64, message = "Publisher must be between 2 and 64 characters")
    private String publisher;

    @Min(value = 0)
    private int totalQuantity;

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

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", totalQuantity='" + totalQuantity + '\'' +
                '}';
    }
}
