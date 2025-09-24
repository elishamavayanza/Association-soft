package com.org.testApi.controllers;

import com.org.testApi.models.Member;
import com.org.testApi.models.User;
import com.org.testApi.services.AuthService;
import com.org.testApi.services.MemberService;
import com.org.testApi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MemberService memberService;
    
    @GetMapping
    public String test() {
        return "Test endpoint working";
    }
    
    @GetMapping("/user")
    public String getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                Collection authorities = authentication.getAuthorities();
                return "Current user: " + username + ", Authorities: " + authorities;
            }
            return "No authenticated user";
        } catch (Exception e) {
            return "Error getting current user: " + e.getMessage();
        }
    }
    
    @GetMapping("/auth-user")
    public ResponseEntity<User> getAuthenticatedUser() {
        try {
            User user = authService.getCurrentUser();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/test-db")
    public String testDatabase() {
        try {
            // This is just a simple test to see if we can access the database
            List<User> users = userService.getAllUsers();
            return "Database connection working. Found " + users.size() + " users.";
        } catch (Exception e) {
            return "Database connection error: " + e.getMessage();
        }
    }
    
    @GetMapping("/test-member")
    public ResponseEntity<String> testMemberCreation() {
        try {
            // Simple test to see if we can create a member
            return ResponseEntity.ok("Member creation test endpoint");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/members")
    public ResponseEntity<List<Member>> getAllMembers() {
        try {
            List<Member> members = memberService.getAllMembers();
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}