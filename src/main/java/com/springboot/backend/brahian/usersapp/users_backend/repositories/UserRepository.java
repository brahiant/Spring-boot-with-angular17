package com.springboot.backend.brahian.usersapp.users_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

import com.springboot.backend.brahian.usersapp.users_backend.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Page<User> findAll(Pageable pageable);
    Optional<User> findByUsername(String username);

}
