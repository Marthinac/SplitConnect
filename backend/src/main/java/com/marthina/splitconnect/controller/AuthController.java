package com.marthina.splitconnect.controller;

import com.marthina.splitconnect.dto.LoginRequestDTO;
import com.marthina.splitconnect.security.auth.UserPrincipal;
import com.marthina.splitconnect.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authManager;
    @Autowired private JwtTokenProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserPrincipal user = (UserPrincipal) auth.getPrincipal();
        String jwt = jwtProvider.generateToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        response.put("userId", user.getId().toString());
        return ResponseEntity.ok(response);
    }
}
