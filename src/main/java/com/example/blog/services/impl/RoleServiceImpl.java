package com.example.blog.services.impl;

import com.example.blog.config.RoleConstants;
import com.example.blog.entity.Role;
import com.example.blog.exceptions.ResourceNotFoundException;
import com.example.blog.repository.RoleRepository;
import com.example.blog.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role getRoleByName(String roleName) {
        switch (roleName) {
            case RoleConstants.ROLE_SUPER_ADMIN:
                return this.roleRepository.findById(RoleConstants.ROLE_SUPER_ADMIN_ID)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "name:"+roleName,0 ));
            case RoleConstants.ROLE_ADMIN:
                return this.roleRepository.findById(RoleConstants.ROLE_ADMIN_ID)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "name"+roleName,0));
            case RoleConstants.ROLE_NORMAL:
                return this.roleRepository.findById(RoleConstants.ROLE_NORMAL_ID)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "name"+roleName,0));
            case RoleConstants.ROLE_VIEWER:
                return this.roleRepository.findById(RoleConstants.ROLE_VIEWER_ID)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "name"+roleName,0));
            default:
                throw new IllegalArgumentException("Invalid role name: " + roleName);
        }
    }
}
