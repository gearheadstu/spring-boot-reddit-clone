package org.kellyjones.videos.redditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest {
    private Long postId;
    private String subredditName;
    private String postTitle;
    private String url;
    private String description;

}
