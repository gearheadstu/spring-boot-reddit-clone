package org.kellyjones.videos.redditclone.services;

import lombok.AllArgsConstructor;
import org.kellyjones.videos.redditclone.dto.RegisterRequest;
import org.kellyjones.videos.redditclone.exceptions.RedditException;
import org.kellyjones.videos.redditclone.model.User;
import org.kellyjones.videos.redditclone.model.VerificationToken;
import org.kellyjones.videos.redditclone.repository.UserRepository;
import org.kellyjones.videos.redditclone.repository.VerificationTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    // FIXME This method is SUPER weak, without controls, validations, or error handling
    public boolean signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);

        return true;
    }

    private String generateVerificationToken(User user) {
        String tokenValue = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(tokenValue);

        verificationTokenRepository.save(verificationToken);

        return tokenValue;
    }

    // FIXME As with signup, this is written VERY optimistically and has ZERO safeguards in place.
    public boolean verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new RedditException("Invalid Token")));
        return true;
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RedditException("User not found with name - " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }
}
