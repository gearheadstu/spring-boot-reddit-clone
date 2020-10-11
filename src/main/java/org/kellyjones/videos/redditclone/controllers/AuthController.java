package org.kellyjones.videos.redditclone.controllers;

import lombok.AllArgsConstructor;
import org.kellyjones.videos.redditclone.dto.RegisterRequest;
import org.kellyjones.videos.redditclone.services.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public void signup(@RequestBody RegisterRequest registerRequest) {

    }
}
