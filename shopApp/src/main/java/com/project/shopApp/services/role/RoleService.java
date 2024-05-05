package com.project.shopApp.services.role;

import com.project.shopApp.models.Role;
import com.project.shopApp.repositories.RoleRepository;
import com.project.shopApp.services.role.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;
    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }
}
