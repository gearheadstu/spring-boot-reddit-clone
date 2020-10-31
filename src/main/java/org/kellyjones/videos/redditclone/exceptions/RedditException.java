package org.kellyjones.videos.redditclone.exceptions;

public class RedditException extends RuntimeException {
    public RedditException(String message, Exception exception) {
        super(message, exception);
    }

    public RedditException(String message) {
        super(message);
    }
}
