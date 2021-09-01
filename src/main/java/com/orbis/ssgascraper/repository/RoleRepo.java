package com.orbis.ssgascraper.repository;

import com.orbis.ssgascraper.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
