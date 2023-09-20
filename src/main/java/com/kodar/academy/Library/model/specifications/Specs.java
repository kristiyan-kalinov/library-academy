package com.kodar.academy.Library.model.specifications;

import com.kodar.academy.Library.model.entity.Author;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.entity.Genre;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class Specs {

    public static Specification<Book> fieldLike(String field, String value) {
        return (root, query, builder) -> builder.like(root.get(field), "%" + value + "%");
    }
    public static Specification<Book> yearBefore(Short yearBefore) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("year"), yearBefore);
    }
    public static Specification<Book> yearAfter(Short yearAfter) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("year"), yearAfter);
    }
    public static Specification<Book> hasGenre(int id) {
        return (root, query, builder) -> {
            Join<Genre, Book> bookGenre = root.join("genres");
            return builder.equal(bookGenre.get("id"), id);
        };
    }
    public static Specification<Book> nameEqual(String field, String name) {
        return (root, query, builder) -> {
            Join<Author, Book> bookAuthor = root.join("authors");
            return builder.equal(bookAuthor.get(field), name);
        };
    }

}
