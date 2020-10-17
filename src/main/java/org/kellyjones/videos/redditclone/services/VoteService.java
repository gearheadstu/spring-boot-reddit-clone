package org.kellyjones.videos.redditclone.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kellyjones.videos.redditclone.dto.VoteDto;
import org.kellyjones.videos.redditclone.exceptions.PostNotFoundException;
import org.kellyjones.videos.redditclone.exceptions.RedditException;
import org.kellyjones.videos.redditclone.model.Post;
import org.kellyjones.videos.redditclone.model.Vote;
import org.kellyjones.videos.redditclone.model.VoteType;
import org.kellyjones.videos.redditclone.repository.PostRepository;
import org.kellyjones.videos.redditclone.repository.VoteRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.kellyjones.videos.redditclone.model.VoteType.UP;

@Service
@Slf4j
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new RedditException("You have already "
                    + voteDto.getVoteType() + "'d for this post");
        }

        VoteType voteType = voteDto.getVoteType();
        post.setVoteCount(post.getVoteCount() + voteType.getDirection());

        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
