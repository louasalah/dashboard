package com.example.DiscountBackend.Services;

import com.example.DiscountBackend.Repository.UserRepository;
import com.example.DiscountBackend.dto.AuthRequest;
import com.example.DiscountBackend.dto.AuthResponse;
import com.example.DiscountBackend.dto.LoginRequest;
import com.example.DiscountBackend.dto.RegisterRequest;
import com.example.DiscountBackend.entities.Role;
import com.example.DiscountBackend.entities.User;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for authentication operations
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    /**
     * Register a new user
     */
    public AuthResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
        	    .email(request.getEmail())
        	    .username(request.getEmail())
        	    .lastName(request.getUsername())
        	    .firstName(request.getUsername())  // Set firstName to the same value for now
        	    .password(passwordEncoder.encode(request.getPassword()))
        	    .role(Role.ADMIN)
        	    .build();
        
        System.out.println("Registering user with email: " + user.getEmail() + " | role: " + user.getRole());

        userRepository.save(user);

        // Generate JWT token
        String token = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        java.util.Collections.emptyList()
                )
        );

        return AuthResponse.builder()
                .token(token)
                .build();
    }
    /**
     * Login a user
     */
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String token = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        java.util.Collections.emptyList()
                )
        );
        
        return AuthResponse.builder()
                .token(token)
                .build();
    }
}