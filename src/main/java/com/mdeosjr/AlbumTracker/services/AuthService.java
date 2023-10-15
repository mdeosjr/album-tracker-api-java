package com.mdeosjr.AlbumTracker.services;

import com.mdeosjr.AlbumTracker.domain.dtos.LoginDTO;
import com.mdeosjr.AlbumTracker.domain.models.User;
import com.mdeosjr.AlbumTracker.repositories.UserRepository;
import com.mdeosjr.AlbumTracker.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    public String login(LoginDTO login) {
        authenticationManager = context.getBean(AuthenticationManager.class);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login.email(), login.password());

        Authentication authenticate = this.authenticationManager.authenticate(authenticationToken);

        var user = (User) authenticate.getPrincipal();

        return tokenService.generateToken(user);
    }
}
