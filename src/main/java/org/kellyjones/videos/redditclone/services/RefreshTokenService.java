package org.kellyjones.videos.redditclone.services;

import lombok.AllArgsConstructor;
import org.kellyjones.videos.redditclone.exceptions.RedditException;
import org.kellyjones.videos.redditclone.model.RefreshToken;
import org.kellyjones.videos.redditclone.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        return refreshTokenRepository.save(refreshToken);
    }

    void validateRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RedditException("Invalid refresh Token"));
    }

    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
