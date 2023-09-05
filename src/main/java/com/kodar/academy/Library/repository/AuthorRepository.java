package com.kodar.academy.Library.repository;

import com.kodar.academy.Library.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);

}
