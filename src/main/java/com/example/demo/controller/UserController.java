package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String username,@RequestParam String password) {
        userService.login(username, password);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(User user) {
        userService.register(user);
        return ResponseEntity.ok(user);

    }
}
