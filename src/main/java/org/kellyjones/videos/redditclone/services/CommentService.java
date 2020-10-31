package org.kellyjones.videos.redditclone.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kellyjones.videos.redditclone.dto.CommentsDto;
import org.kellyjones.videos.redditclone.exceptions.PostNotFoundException;
import org.kellyjones.videos.redditclone.mapper.CommentMapper;
import org.kellyjones.videos.redditclone.model.Comment;
import org.kellyjones.videos.redditclone.model.Post;
import org.kellyjones.videos.redditclone.model.User;
import org.kellyjones.videos.redditclone.repository.CommentRepository;
import org.kellyjones.videos.redditclone.repository.PostRepository;
import org.kellyjones.videos.redditclone.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {

    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public void save(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).collect(toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }


}
