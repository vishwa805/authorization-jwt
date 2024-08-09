package com.example.demo.controllers;

import com.example.demo.dto.UserDTO;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.AdminServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    AdminServices adminServices;

    @PostMapping("/create")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse createUser(@RequestBody UserDTO userDTO){
        return adminServices.createUser(userDTO);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse updateUser(@RequestBody UserDTO userDTO){
        return adminServices.updateUser(userDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse deleteUser(@RequestBody Map<String, String> request){
        String email = request.get("email");
        System.out.println(email);
        return adminServices.deleteUser(email);
    }

}
