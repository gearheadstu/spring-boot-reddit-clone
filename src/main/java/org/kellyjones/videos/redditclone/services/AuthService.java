package org.kellyjones.videos.redditclone.services;

import lombok.AllArgsConstructor;
import org.kellyjones.videos.redditclone.dto.RegisterRequest;
import org.kellyjones.videos.redditclone.model.User;
import org.kellyjones.videos.redditclone.model.VerificationToken;
import org.kellyjones.videos.redditclone.repository.UserRepository;
import org.kellyjones.videos.redditclone.repository.VerificationTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);
        // FIXME Several points of apparent weakness here, we're not doing any validations or error handling

        String token = generateVerificationToken(user);
    }

    private String generateVerificationToken(User user) {
        String tokenValue = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(tokenValue);

        verificationTokenRepository.save(verificationToken);

        return tokenValue;

    }
}
