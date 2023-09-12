package com.kodar.academy.Library.model.mapper;

import com.kodar.academy.Library.model.dto.genre.GenreDTO;
import com.kodar.academy.Library.model.entity.Genre;

public class GenreMapper {

    public static GenreDTO mapToResponse(Genre source) {
        GenreDTO target = new GenreDTO();
        target.setName(source.getName());
        return target;
    }

}
