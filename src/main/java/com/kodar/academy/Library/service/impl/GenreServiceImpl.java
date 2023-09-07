package com.kodar.academy.Library.service.impl;

import com.kodar.academy.Library.model.dto.genre.GenreDTO;
import com.kodar.academy.Library.model.mapper.GenreMapper;
import com.kodar.academy.Library.repository.GenreRepository;
import com.kodar.academy.Library.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Set<GenreDTO> getAllGenres() {
        Set<GenreDTO> genres = genreRepository.findAll().stream()
                .map(GenreMapper::mapToResponse)
                .collect(Collectors.toSet());
        return genres;
    }
}
