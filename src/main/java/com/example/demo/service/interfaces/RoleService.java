package com.example.demo.service.interfaces;

import com.example.demo.entity.Role;
import com.example.demo.enumuration.ERole;

import java.util.Optional;

public interface RoleService {
    void saveRole(Role role);
    Optional<Role> findRole(ERole eRole);
}
