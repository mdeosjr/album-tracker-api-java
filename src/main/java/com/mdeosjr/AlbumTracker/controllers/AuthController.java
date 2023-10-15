package com.mdeosjr.AlbumTracker.controllers;

import com.mdeosjr.AlbumTracker.domain.dtos.LoginDTO;
import com.mdeosjr.AlbumTracker.domain.models.User;
import com.mdeosjr.AlbumTracker.security.TokenService;
import com.mdeosjr.AlbumTracker.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> create(@RequestBody User user) {
        userService.createUser(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public String login (@RequestBody @Valid LoginDTO login) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login.email(), login.password());

        Authentication authenticate = this.authenticationManager.authenticate(authenticationToken);

        var user = (User) authenticate.getPrincipal();

        return tokenService.generateToken(user);
    }
}
