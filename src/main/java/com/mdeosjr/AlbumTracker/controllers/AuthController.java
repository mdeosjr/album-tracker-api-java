package com.mdeosjr.AlbumTracker.controllers;

import com.mdeosjr.AlbumTracker.domain.dtos.LoginDTO;
import com.mdeosjr.AlbumTracker.domain.models.User;
import com.mdeosjr.AlbumTracker.services.AuthService;
import com.mdeosjr.AlbumTracker.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> create(@RequestBody User user) {
        userService.createUser(user);

        return new ResponseEntity<>("User created!", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDTO login) {
        var token = authService.login(login);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
