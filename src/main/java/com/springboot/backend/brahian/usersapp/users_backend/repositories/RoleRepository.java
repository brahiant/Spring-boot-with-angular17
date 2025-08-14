package com.springboot.backend.brahian.usersapp.users_backend.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

import com.springboot.backend.brahian.usersapp.users_backend.entities.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
