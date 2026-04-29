package com.turkcell.spring_starter.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.turkcell.spring_starter.dto.LoginRequest;
import com.turkcell.spring_starter.dto.RegisterRequest;
import com.turkcell.spring_starter.entity.User;
import com.turkcell.spring_starter.repository.UserRepository;

@Service
public class UserServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegisterRequest registerRequest) {
        User existingUser = this.userRepository.findByEmail(registerRequest.getEmail()).orElse(null);
        if(existingUser != null) throw new RuntimeException("Email is already in use!");

        User user = new User();
        user.setEmail(registerRequest.getEmail());

        String encodedPassword = this.passwordEncoder.encode(registerRequest.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }

    public String login(LoginRequest loginRequest){
        String errorMessage = "Login credentials are invalid!";

        User user = this.userRepository
                        .findByEmail(loginRequest.getEmail())
                        .orElseThrow(() -> new RuntimeException(errorMessage));
        
        boolean passwordMatch = this.passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        if(!passwordMatch)
            throw new RuntimeException(errorMessage);

        return "Login successful";
    }
}
