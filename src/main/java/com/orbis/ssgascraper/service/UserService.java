package com.orbis.ssgascraper.service;

import com.orbis.ssgascraper.model.Role;
import com.orbis.ssgascraper.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    Role getRole(String role);
    List<User> getUsers();


}
