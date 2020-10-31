package org.kellyjones.videos.redditclone.controllers;

import lombok.AllArgsConstructor;
import org.kellyjones.videos.redditclone.dto.AuthenticationResponse;
import org.kellyjones.videos.redditclone.dto.LoginRequest;
import org.kellyjones.videos.redditclone.dto.RegisterRequest;
import org.kellyjones.videos.redditclone.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        if (authService.signup(registerRequest)) {
            return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User Registration Failed", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("accountVerification/{token}") // FIXME Not sure I love this being a GET operation.
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        if (authService.verifyAccount(token)) {
            return new ResponseEntity<>("Account Activated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unable to Activate Account", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);

    }
}
