package org.kellyjones.videos.redditclone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Holy crap, this is bad practice.")
    private String password;

    @Email
    @NotEmpty(message = "Email address is required")
    private String email;

    private Instant created;

    private boolean enabled;

    // FIXME: Add lots of other stuff:
    // List of posts
    // List of comments
    // List of votes
    // List of saved posts
    // List of users followed
    // List of subreddits followed
    // List of subreddits for which user is a moderator
    // ....I'm probably going to want to burn this model down and rebuild from scratch to my own tastes
}
