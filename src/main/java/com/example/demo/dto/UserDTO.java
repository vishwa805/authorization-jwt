package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {

    @NotBlank(message = "firstname is required")
    private String firstName;

    @NotBlank(message = "lastname is required")
    private String lastName;

    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "role is required")
    private int roleId;

    @NotBlank(message = "permissions is required")
    private List<Integer> permissionId;

}
