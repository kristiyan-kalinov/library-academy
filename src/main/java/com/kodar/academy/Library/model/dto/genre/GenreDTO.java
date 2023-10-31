package com.kodar.academy.Library.model.dto.genre;

import com.kodar.academy.Library.model.validation.ExistingGenre;

public class GenreDTO {

    @ExistingGenre
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
