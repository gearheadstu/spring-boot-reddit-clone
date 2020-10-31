package org.kellyjones.videos.redditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String postTitle;
    private String url;
    private String description;
    private String username;
    private String subredditName;
    private String duration;
    private Integer voteCount;
    private Integer commentCount;
    private boolean upVote;
    private boolean downVote;

}
