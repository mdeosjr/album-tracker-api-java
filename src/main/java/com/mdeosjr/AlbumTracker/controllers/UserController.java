package com.mdeosjr.AlbumTracker.controllers;

import com.mdeosjr.AlbumTracker.domain.models.User;
import com.mdeosjr.AlbumTracker.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody User user) {
        userService.createUser(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
