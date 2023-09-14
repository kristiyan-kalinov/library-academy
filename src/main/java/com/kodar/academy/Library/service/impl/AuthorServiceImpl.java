package com.kodar.academy.Library.service.impl;

import com.kodar.academy.Library.model.dto.author.AuthorDTO;
import com.kodar.academy.Library.model.entity.Author;
import com.kodar.academy.Library.model.mapper.AuthorMapper;
import com.kodar.academy.Library.repository.AuthorRepository;
import com.kodar.academy.Library.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<AuthorDTO> getAllAuthors() {
        logger.info("getAllAuthors called");
        List<AuthorDTO> authors = authorRepository.findAll().stream()
                .map(AuthorMapper::mapToResponse)
                .toList();
        return authors;
    }

    @Override
    public Author addOrFindAuthor(AuthorDTO authorDTO) {
        logger.info("addOrFindAuthor called with params: " + authorDTO.toString());
        Author authorData = authorRepository.findByFirstNameAndLastName(authorDTO.getFirstName(), authorDTO.getLastName())
                .orElse(null);
        if(authorData == null) {
            Author author = new Author();
            author.setFirstName(authorDTO.getFirstName());
            author.setLastName(authorDTO.getLastName());
            return authorRepository.save(author);
        };
        return authorData;
    }
}
