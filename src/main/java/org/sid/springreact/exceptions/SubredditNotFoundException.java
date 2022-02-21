package org.sid.springreact.exceptions;

public class SubredditNotFoundException extends RuntimeException {
    public SubredditNotFoundException(String s) {
        super("Not found Subreddit "+s);
    }
}
