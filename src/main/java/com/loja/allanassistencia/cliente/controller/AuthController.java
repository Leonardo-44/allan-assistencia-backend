package com.loja.allanassistencia.controller;

import com.loja.allanassistencia.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password-hash}")
    private String adminPasswordHash;

    public AuthController(JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null
                || !username.equals(adminUsername)
                || !passwordEncoder.matches(password, adminPasswordHash)) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuário ou senha inválidos"));
        }

        String token = jwtUtil.generateToken(username);
        return ResponseEntity.ok(Map.of("token", token, "username", username));
    }
}