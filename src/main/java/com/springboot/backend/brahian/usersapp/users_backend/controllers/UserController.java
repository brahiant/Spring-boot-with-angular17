package com.springboot.backend.brahian.usersapp.users_backend.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Collections;

import com.springboot.backend.brahian.usersapp.users_backend.entities.User;
import com.springboot.backend.brahian.usersapp.users_backend.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "User not found"));
        }
    }
    

}
