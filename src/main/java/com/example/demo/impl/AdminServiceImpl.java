package com.example.demo.impl;


import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Permissions;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.enums.RoleENUM;
import com.example.demo.exception.CommonException;
import com.example.demo.repository.PermissionsRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.AdminServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AdminServiceImpl implements AdminServices {

    @Autowired
    PermissionsRepository permissionsRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse createUser(UserDTO userDTO) {

        // Validating role id
        Role role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(() -> new CommonException("Invalid Role Id " + userDTO.getRoleId()));

        // Validating user email
        Optional<User> user = userRepository.findByEmail(userDTO.getEmail());

        if(user.isPresent()){
            throw new CommonException("Email Id already exist");
        }

        User createUser = new User();
        createUser.setFirstName(userDTO.getFirstName());
        createUser.setLastName(userDTO.getLastName());
        createUser.setEmail(userDTO.getEmail());
        createUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        Set<Role> userRoles = new HashSet<>();

        userRoles.add(role);

        createUser.setRoles(userRoles);

        userRepository.save(createUser);

        return new ApiResponse(200, "User Created Successfully", null);
    }

    @Override
    public ApiResponse updateUser(UserDTO userDTO) {

        Role role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(() -> new CommonException("Invalid Role Id " + userDTO.getRoleId()));

        User existingUser = userRepository.findByEmail(userDTO.getEmail()).orElseThrow(() -> new CommonException("Invalid Email Id"));

        // Updating user
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        Set<Role> userRoles = new HashSet<>();

        userRoles.add(role);

        existingUser.setRoles(userRoles);

        userRepository.save(existingUser);

        return new ApiResponse(200, "User updated Successfully", null);

    }

    @Override
    public ApiResponse deleteUser(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new CommonException("Invalid Email Id"));

        userRepository.deleteById(user.getId());

        return new ApiResponse(200, "User Deleted Successfully", null);
    }


}