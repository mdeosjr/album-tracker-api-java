package com.mdeosjr.AlbumTracker.services;

import com.mdeosjr.AlbumTracker.domain.models.User;
import com.mdeosjr.AlbumTracker.repositories.UserRepository;
import com.mdeosjr.AlbumTracker.services.exceptions.UserAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public void createUser(User user) {
        var existentUser = userRepository.findByEmail(user.getEmail());
        if (existentUser != null) throw new UserAlreadyExistsException();

        String password = user.getPassword();
        user.setPassword(encoder.encode(password));
        userRepository.save(user);
    }
}
