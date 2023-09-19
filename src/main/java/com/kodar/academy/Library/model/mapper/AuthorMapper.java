package com.kodar.academy.Library.model.mapper;

import com.kodar.academy.Library.model.dto.author.AuthorDTO;
import com.kodar.academy.Library.model.entity.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorMapper {

    private static final Logger logger = LoggerFactory.getLogger(AuthorMapper.class);
    public static AuthorDTO mapToResponse(Author source) {
        logger.info("mapToResponse called");
        AuthorDTO target = new AuthorDTO();
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        return target;
    }

}
