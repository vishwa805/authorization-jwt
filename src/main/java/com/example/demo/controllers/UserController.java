package com.example.demo.controllers;

import com.example.demo.entity.User;
import com.example.demo.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserServices userServices;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/home")
    public Map<String, String> home(){
        Map<String, String> responses = new HashMap<>();
        responses.put("result", "Welcome to home page");
        return responses;
    }

    @PostMapping("/auth/register")
    public Map<String, String> register(@RequestBody @Valid User user){
        return userServices.register(user);
    }

    @PostMapping("/auth/login")
    public Map<String, String> login(@RequestBody User user){
        System.out.println("Inside Controller ==================> ");
        String username = user.getUsername();
        String password = user.getPassword();
        return userServices.login(username, password);
    }

    @GetMapping("/user")
    public Map<String, Object> getUserInformation(){
        Map<String, Object> response = new HashMap<>();
        try{
            System.out.println("Inside Controller ==================> ");
            response.put("result", userServices.getUserData());
            return response;
        }catch(Exception e){
            System.out.println("Catch Block ============> " + e);
            response.put("error", e.getMessage());
            return response;
        }

    }

}