package com.orbis.ssgascraper.repository;

import com.orbis.ssgascraper.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
