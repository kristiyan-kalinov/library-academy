package com.kodar.academy.Library.repository;

import com.kodar.academy.Library.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
