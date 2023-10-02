package com.kodar.academy.Library.service;

import com.kodar.academy.Library.model.dto.genre.GenreDTO;
import com.kodar.academy.Library.model.mapper.GenreMapper;
import com.kodar.academy.Library.repository.GenreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private static final Logger logger = LoggerFactory.getLogger(GenreService.class);

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Set<GenreDTO> getAllGenres() {
        logger.info("getAllGenres called");
        Set<GenreDTO> genres = genreRepository.findAll().stream()
                .map(GenreMapper::mapToResponse)
                .collect(Collectors.toSet());
        return genres;
    }
}
