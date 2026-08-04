package com.hoehn.game.repository;

import com.hoehn.game.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Retrieves a user from the database given the username
    Optional<User> findByUserName(String userName);

}
