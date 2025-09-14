package com.example.demo.service;

import com.example.demo.dtos.output.UserResponseDTO;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.utils.customexceptions.RequiredResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService extends BaseService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public List<UserResponseDTO> getUsersByRole(String roleId) throws Exception {
        try {
            Role role = roleRepository.getRoleById(roleId)
                    .orElseThrow(()->new RequiredResourceNotFoundException("Role not found"));
            return role.getUsers().stream().map(user -> UserResponseDTO.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .full_name(user.getFullName())
                            .mobile_number(user.getMobileNumber())
                            .role_id(role.getId())
                            .build())
                    .toList();
        }catch (Exception e){
            processException(e);
        }
        return new ArrayList<>();

    }
    public List<UserResponseDTO> getAllUsers() throws Exception {
        try {
            return userRepository.findAll().stream().map(user -> UserResponseDTO.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .full_name(user.getFullName())
                            .mobile_number(user.getMobileNumber())
                            .role_id(getRoles(user))
                            .build())
                    .toList();
        }catch (Exception e){
            processException(e);
        }
        return new ArrayList<>();

    }
    private String getRoles(User user){
        return StringUtils.join(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()), ",");
    }

    public UserResponseDTO getUserById(String userId) throws Exception {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(()->new RequiredResourceNotFoundException("User not found"));
            return UserResponseDTO.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .full_name(user.getFullName())
                    .mobile_number(user.getMobileNumber())
                    .role_id(getRoles(user))
                    .build();
        }catch (Exception e){
            processException(e);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteUserById(String userId) throws Exception {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(()->new RequiredResourceNotFoundException("User not found"));
            userRepository.delete(user);
        }catch (Exception e){
            processException(e);
        }
    }


}
