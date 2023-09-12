package com.kodar.academy.Library.service;

import com.kodar.academy.Library.model.dto.genre.GenreDTO;

import java.util.Set;

public interface GenreService {

    Set<GenreDTO> getAllGenres();

}
