package org.sid.springreact.repository;

import org.sid.springreact.entities.Post;
import org.sid.springreact.entities.Subreddit;
import org.sid.springreact.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findAllByUser(User user);
}
