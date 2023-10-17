package com.kodar.academy.Library.repository;

import com.kodar.academy.Library.model.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentRepository extends JpaRepository<Rent, Integer> {

    Optional<Rent> findByBookIdAndUserId(int bookId, int userId);

}
