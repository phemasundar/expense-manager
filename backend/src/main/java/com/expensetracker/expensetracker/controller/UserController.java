package com.expensetracker.expensetracker.controller;

import com.expensetracker.expensetracker.entity.User;
import com.expensetracker.expensetracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        String clerkUserId = jwt.getSubject();
        String email = jwt.getClaim("email"); // Assuming 'email' claim is available in JWT

        User user = userService.findOrCreateUser(clerkUserId, email);
        return ResponseEntity.ok(user);
    }
}
