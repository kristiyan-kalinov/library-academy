package com.kodar.academy.Library.model.specifications;

import com.kodar.academy.Library.model.constants.Constants;
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
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get(Constants.YEAR), yearBefore);
    }
    public static Specification<Book> yearAfter(Short yearAfter) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get(Constants.YEAR), yearAfter);
    }
    public static Specification<Book> hasGenre(int id) {
        return (root, query, builder) -> {
            Join<Genre, Book> bookGenre = root.join(Constants.GENRES);
            return builder.equal(bookGenre.get(Constants.ID), id);
        };
    }
    public static Specification<Book> nameEqual(String field, String name) {
        return (root, query, builder) -> {
            Join<Author, Book> bookAuthor = root.join(Constants.AUTHORS);
            return builder.equal(bookAuthor.get(field), name);
        };
    }
    public static Specification<Book> isActive(boolean isActive) {
        return (root, query, builder) -> builder.equal(root.get(Constants.IS_ACTIVE), isActive);
    }

}
