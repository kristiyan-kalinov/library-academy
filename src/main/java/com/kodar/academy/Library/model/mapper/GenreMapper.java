package com.kodar.academy.Library.model.mapper;

import com.kodar.academy.Library.model.dto.genre.GenreDTO;
import com.kodar.academy.Library.model.entity.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenreMapper {

    private static final Logger logger = LoggerFactory.getLogger(GenreMapper.class);
    public static GenreDTO mapToResponse(Genre source) {
        logger.info("mapToResponse called");
        GenreDTO target = new GenreDTO();
        target.setName(source.getName());
        return target;
    }

}
