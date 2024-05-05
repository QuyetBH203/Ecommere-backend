package com.project.shopApp.controllers;


import com.project.shopApp.models.Role;
import com.project.shopApp.services.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping("")
    public ResponseEntity<?> getAllRole(){
        List<Role> roles=roleService.getAllRole();
        return ResponseEntity.ok(roles);
    }

}
