package com.springboot.backend.brahian.usersapp.users_backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.springboot.backend.brahian.usersapp.users_backend.entities.User;

@Service
public interface UserService {

    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User createUser(User user);
    User updateUser(User user);
    void deleteById(Long id);

}
