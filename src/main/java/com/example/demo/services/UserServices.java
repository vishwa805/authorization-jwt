package com.example.demo.services;

import com.example.demo.entity.User;

import java.util.Map;

public interface UserServices {

    Map<String, String> register(User user);

    Map<String, String> login(String username, String password);

    User getUserData();

    String updateProfile();

    String deleteProfile();

}