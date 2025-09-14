package com.example.demo.service;

import com.example.demo.dtos.output.RoleDTO;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RoleService extends BaseService {
    private final RoleRepository roleRepository;

    public List<String> getAuthoritiesFromUser(User user) {
        List<String> authorities = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            authorities.add(role.getName());
        }
        return authorities;
    }
    public List<RoleDTO> getAllRoles() throws Exception {
        try {
            List<Role> roles = roleRepository.findAll();
            return roles.stream().map(role -> RoleDTO.builder()
                    .role_id(role.getId())
                    .role_name(role.getName())
                    .build()).collect(Collectors.toList());
        } catch (Exception e) {
            processException(e);
        }
        return new ArrayList<>();
    }
    public RoleDTO getRoleById(String roleId) throws Exception {
        try {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new Exception("Role not found"));
            return RoleDTO.builder()
                    .role_id(role.getId())
                    .role_name(role.getName())
                    .build();
        } catch (Exception e) {
            processException(e);
        }
        return null;
    }
    public void deleteRoleById(String roleId) throws Exception {
        try {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new Exception("Role not found"));
            roleRepository.delete(role);
        } catch (Exception e) {
            processException(e);
        }
    }
}
