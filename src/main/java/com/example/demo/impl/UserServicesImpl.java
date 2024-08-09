package com.example.demo.impl;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.jwt.JwtHelper;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserServices;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserServicesImpl implements UserServices {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(User user) {

        log.info("Control in register business logic .....");

        Optional<User> userFound = userRepository.findByEmail(user.getEmail());

        Map<String, String> response = new HashMap<>();

        if(userFound.isPresent()){
            response.put("result", "User already exist with this email");
            return response;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        response.put("result", "Successfully registered");

        return response;
    }

    @Override
    public Map<String, String> login(String username, String password) {

        log.info("Control in login business logic .......");

        System.out.println(username);

        User userFound = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Invalid email id"));

        System.out.println(userFound.getEmail() + userFound.getPassword());

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }catch(BadCredentialsException e){
            throw new BadCredentialsException("Invalid Username and password");
        }

        Map<String, String> response = new HashMap<>();

        Map<String, Object> claims = new HashMap<>();

        Optional<Role> userRole = userFound.getRoles().stream().findFirst();

        claims.put("role", userRole.get().getName());

        String jwt = JwtHelper.generateToken(claims, username);

        response.put("token", jwt);

        return response;

//        Map<String, String> response = new HashMap<>();

       // System.out.println(userFound.get());

//        if(userFound.isPresent()){
//
//            if(!userFound.get().getPassword().equals(password)){
//                response.put("result", "Incorrect Password");
//                return response;
//            }
//
//            response.put("result", "Login successfully");
//
//            HashMap<String, Object> claims = new HashMap<>();
//
//            claims.put("type", "USER");
//
//            String jwt = JwtHelper.generateToken(claims, username);
//
//            response.put("token", jwt);
//
//            return response;
//
//        }

//        response.put("result", "Please register first");
//
//        return response;
    }

    @Override
    public User getUserData() {
        System.out.println("Inside User Service ===================> ");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Check if the principal is an instance of your custom User class
        if (principal instanceof User) {
            User user = (User) principal;
            System.out.println("Inside User ============> " + user);
            return User.builder().firstName(user.getFirstName()).lastName(user.getLastName())
                    .email(user.getEmail()).build();
        } else if (principal instanceof String) {
            // This case handles if the principal is a username (String)
            String username = (String) principal;
            System.out.println("Principal is a username: " + username);
            // Fetch the user details from your user service or repository
            // Example: User user = userService.findByUsername(username);
            // return User.builder()...build();

            // Handle this appropriately, possibly fetching the user details from your database
        } else {
            // Handle other cases or unexpected types
            System.out.println("Principal is of unexpected type: " + principal.getClass());
        }

        return null; // or handle this case appropriately
    }


    @Override
    public String updateProfile() {
        return "";
    }

    @Override
    public String deleteProfile() {
        return "";
    }
}
