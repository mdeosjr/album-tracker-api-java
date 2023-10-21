package com.mdeosjr.AlbumTracker.services;

import com.mdeosjr.AlbumTracker.domain.dtos.LoginDTO;
import com.mdeosjr.AlbumTracker.domain.dtos.SpotifyLoginDTO;
import com.mdeosjr.AlbumTracker.domain.models.User;
import com.mdeosjr.AlbumTracker.repositories.UserRepository;
import com.mdeosjr.AlbumTracker.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.net.URI;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class AuthService implements UserDetailsService {
    @Value("${spotify.client.id}")
    private String CLIENT_ID;
    @Value("${spotify.client.secret}")
    private String CLIENT_SECRET;
    @Value("${spotify.redirect.uri}")
    private URI REDIRECT_URI;

    @Autowired
    private ApplicationContext context;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = (User) userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException("User not found!");

        return user;
    }

    public String login(LoginDTO login) {
        AuthenticationManager authenticationManager = context.getBean(AuthenticationManager.class);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login.email(), login.password());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        var user = (User) authenticate.getPrincipal();

        return tokenService.generateToken(user);
    }

    public String spotifyLogin(SpotifyLoginDTO spotifyLogin) {
        User newUser = new User();

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(CLIENT_ID)
                .setClientSecret(CLIENT_SECRET)
                .setRedirectUri(REDIRECT_URI)
                .build();

        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(spotifyLogin.code())
                .build();

        try {
            final CompletableFuture<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture = authorizationCodeRequest.executeAsync();

            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeCredentialsFuture.join();

            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }

        GetCurrentUsersProfileRequest getUserProfile = spotifyApi.getCurrentUsersProfile().build();

        try {
            final CompletableFuture<se.michaelthelin.spotify.model_objects.specification.User> userFuture = getUserProfile.executeAsync();
            final se.michaelthelin.spotify.model_objects.specification.User user = userFuture.join();

            newUser.setName(user.getDisplayName());
            newUser.setEmail(user.getEmail());
            newUser.setProvider("spotify");
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }

        var savedUser = userRepository.save(newUser);
        return tokenService.generateToken(savedUser);
    }
}
