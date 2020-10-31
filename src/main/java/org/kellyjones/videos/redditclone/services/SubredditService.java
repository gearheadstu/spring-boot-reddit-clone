package org.kellyjones.videos.redditclone.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kellyjones.videos.redditclone.dto.SubredditDto;
import org.kellyjones.videos.redditclone.exceptions.RedditException;
import org.kellyjones.videos.redditclone.mapper.SubredditMapper;
import org.kellyjones.videos.redditclone.model.Subreddit;
import org.kellyjones.videos.redditclone.repository.SubredditRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(save.getId());
        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        // FIXME This seems obviously problematic for anything more than HelloWorld purposes
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(toList());
    }

    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new RedditException("No subreddit for ID " + id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }

    public SubredditDto getSubreddit(String name) {
        Subreddit subreddit = subredditRepository.findByName(name)
                .orElseThrow(() -> new RedditException("No subreddit for name " + name));
        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
