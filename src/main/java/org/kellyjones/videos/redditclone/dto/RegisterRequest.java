package org.kellyjones.videos.redditclone.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String username;
    private String email;
    private String password;
}
