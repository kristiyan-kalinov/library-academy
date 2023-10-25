package com.kodar.academy.Library.repository;

import com.kodar.academy.Library.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    List<User> findAllBySubscriptionNotNull();
    @Query("""
            SELECT u FROM User u
            JOIN Rent AS r on u.id=r.user.id
            WHERE r.returnDate IS NULL
            AND r.expectedReturnDate < CURDATE()
            """)
    List<User> findAllProlonged();
}
