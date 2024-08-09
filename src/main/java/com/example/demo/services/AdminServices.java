package com.example.demo.services;

import com.example.demo.dto.UserDTO;
import com.example.demo.response.ApiResponse;

public interface AdminServices {

    public ApiResponse createUser(UserDTO userDTO);

    public ApiResponse updateUser(UserDTO userDTO);

    public ApiResponse deleteUser(String email);

}
