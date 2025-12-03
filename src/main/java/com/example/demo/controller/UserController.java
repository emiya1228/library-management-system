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
    public ResponseEntity<?> login(@RequestParam String username,@RequestParam String password) {
        String login = userService.login(username, password);
        if (login != null) {
            return ResponseEntity.ok().body(login);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(User user) {
        userService.register(user);
        return ResponseEntity.ok(user);

    }
}
