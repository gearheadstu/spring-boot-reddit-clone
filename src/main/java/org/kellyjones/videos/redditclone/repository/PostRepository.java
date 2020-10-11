package org.kellyjones.videos.redditclone.repository;

import org.kellyjones.videos.redditclone.model.Post;
import org.kellyjones.videos.redditclone.model.Subreddit;
import org.kellyjones.videos.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findAllByUser(User user);
}
