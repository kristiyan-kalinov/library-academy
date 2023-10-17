package com.kodar.academy.Library.model.dto.book;

import com.kodar.academy.Library.model.constants.Constants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BookEditRequestDTO {

    @NotBlank(message = Constants.TITLE_REQUIRED)
    @Size(min = 1, max = 255, message = Constants.TITLE_LENGTH)
    private String title;

    @NotBlank(message = Constants.PUBLISHER_REQUIRED)
    @Size(min = 2, max = 64, message = Constants.PUBLISHER_LENGTH)
    private String publisher;

    @Min(value = 0, message = Constants.TOTAL_QUANTITY_MIN_VALUE)
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
