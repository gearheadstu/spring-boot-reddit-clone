package org.kellyjones.videos.redditclone.model;

import org.kellyjones.videos.redditclone.exceptions.RedditException;

import java.util.Arrays;

public enum VoteType {
    UP(1),
    DOWN(-1);

    private final int direction;

    VoteType(int direction) {
        this.direction = direction;
    }

    public Integer getDirection() {
        return direction;
    }

    public static VoteType lookup(Integer direction) {
        return Arrays.stream(VoteType.values())
                .filter(value -> value.getDirection().equals(direction))
                .findAny()
                .orElseThrow(() -> new RedditException("Failed to locate request VoteType")); // FIXME, format String to show input
    }
}
