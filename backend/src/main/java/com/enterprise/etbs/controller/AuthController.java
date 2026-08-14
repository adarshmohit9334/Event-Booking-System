package com.enterprise.etbs.controller;

import com.enterprise.etbs.dto.request.LoginRequest;
import com.enterprise.etbs.dto.request.SignupRequest;
import com.enterprise.etbs.dto.response.LoginResponse;
import com.enterprise.etbs.entity.User;
import com.enterprise.etbs.repository.UserRepository;
import com.enterprise.etbs.security.jwt.JwtUtils;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostConstruct
    public void seedDefaultUsers() {
        if (userRepository.count() == 0) {
            userRepository.save(new User(null, "Administrator", "admin@eventify.com", passwordEncoder.encode("admin123"), "admin"));
            userRepository.save(new User(null, "John Doe", "john.doe@gmail.com", passwordEncoder.encode("john123"), "customer"));
            userRepository.save(new User(null, "Sarah Smith", "sarah.s@yahoo.com", passwordEncoder.encode("sarah123"), "customer"));
            System.out.println("Default users seeded successfully.");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: Email is already in use!"));
        }

        User user = User.builder()
                .name(signUpRequest.getName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(signUpRequest.getRole())
                .build();

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully!"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Error: User not found!"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error: Invalid password!"));
        }

        String jwt = jwtUtils.generateJwtToken(user.getEmail(), user.getRole());

        return ResponseEntity.ok(new LoginResponse(
                jwt,
                user.getName(),
                user.getEmail(),
                user.getRole()
        ));
    }
}
