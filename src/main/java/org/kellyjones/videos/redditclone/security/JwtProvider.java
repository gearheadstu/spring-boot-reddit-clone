package org.kellyjones.videos.redditclone.security;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.kellyjones.videos.redditclone.exceptions.RedditException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;

import io.jsonwebtoken.Jwts;

import static io.jsonwebtoken.Jwts.parser;
import static java.util.Date.from;

@Service
@Slf4j
public class JwtProvider {

    private KeyStore keystore;

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

    @Value("${keystore.secret}")
    private String keystoreSecret;

    @Value("${keystore.path}")
    private String keystorePath;

    @Value("${keystore.jwt.key}")
    private String jwtKeyName;

    @PostConstruct
    public void init() {
        try {
            keystore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream(keystorePath);
            keystore.load(resourceAsStream, keystoreSecret.toCharArray());
        } catch(KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new RedditException("Exception occurred while loading keystore", e);
        }
    }

    public String generateToken(Authentication authentication) {
        User principal = (User)authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();

    }

    public String generateTokenWithUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    private PublicKey getPublicKey() {
        try {
            return keystore.getCertificate(jwtKeyName).getPublicKey();
        } catch (KeyStoreException e) {
            throw new RedditException("Exception while retrieving public key from keystore", e);
        }
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keystore.getKey(jwtKeyName, keystoreSecret.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new RedditException("Exception while retrieving private key from keystore", e);
        }
    }

    public Long getJwtExpirationInMillis() {
        return jwtExpirationInMillis;
    }

    public boolean validateToken(String jwt) {
        parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
        return true; // FIXME This is SUPER WEAK.
    }

    public String getUsernameFromJwt(String jwt) {
        Claims claims = parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(jwt)
                .getBody();

        return claims.getSubject();
    }
}
