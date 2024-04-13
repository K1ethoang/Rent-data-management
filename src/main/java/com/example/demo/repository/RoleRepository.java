package com.example.demo.repository;

import com.example.demo.entity.Role;
import com.example.demo.enumuration.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleByName(ERole roleName);
}
