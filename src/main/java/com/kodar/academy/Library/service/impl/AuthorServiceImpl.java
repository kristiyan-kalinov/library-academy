package com.kodar.academy.Library.service.impl;

import com.kodar.academy.Library.model.dto.author.AuthorDTO;
import com.kodar.academy.Library.model.mapper.AuthorMapper;
import com.kodar.academy.Library.repository.AuthorRepository;
import com.kodar.academy.Library.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<AuthorDTO> getAllAuthors() {
        List<AuthorDTO> authors = authorRepository.findAll().stream()
                .map(AuthorMapper::mapToResponse)
                .toList();
        return authors;
    }
}
