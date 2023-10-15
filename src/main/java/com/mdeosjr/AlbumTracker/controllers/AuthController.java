package com.mdeosjr.AlbumTracker.controllers;

import com.mdeosjr.AlbumTracker.domain.dtos.LoginDTO;
import com.mdeosjr.AlbumTracker.domain.models.User;
import com.mdeosjr.AlbumTracker.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public String login (@RequestBody LoginDTO login) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login.email(), login.password());

        Authentication authenticate = this.authenticationManager.authenticate(authenticationToken);

        var user = (User) authenticate.getPrincipal();

        return tokenService.generateToken(user);
    }
}
