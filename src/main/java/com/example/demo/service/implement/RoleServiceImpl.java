package com.example.demo.service.implement;

import com.example.demo.entity.Role;
import com.example.demo.enumuration.ERole;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.interfaces.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public Optional<Role> findRole(ERole eRole) {
        return roleRepository.findRoleByName(eRole);
    }
}
