package com.velikanovdev.platform.service;

import com.velikanovdev.platform.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    void registerUser(User user);
    User getUserByUsername(String username);
    List<User> getAllUsers();
}
