package com.springboot.backend.brahian.usersapp.users_backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.backend.brahian.usersapp.users_backend.entities.User;
import com.springboot.backend.brahian.usersapp.users_backend.models.UserRequest;

@Service
public interface UserService {

    List<User> getAllUsers();
    Page<User> getAllUsers(Pageable pageable);
    Optional<User> getUserById(Long id);
    User createUser(User user);
    Optional<User> updateUser(UserRequest userRequest, Long id);
    void deleteById(Long id);

}
