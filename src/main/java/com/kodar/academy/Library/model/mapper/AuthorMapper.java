package com.kodar.academy.Library.model.mapper;

import com.kodar.academy.Library.model.dto.author.AuthorDTO;
import com.kodar.academy.Library.model.entity.Author;

public class AuthorMapper {

    public static AuthorDTO mapToResponse(Author source) {
        AuthorDTO target = new AuthorDTO();
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        return target;
    }

}
