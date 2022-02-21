package org.sid.springreact.repository;

import org.sid.springreact.entities.Comment;
import org.sid.springreact.entities.Post;
import org.sid.springreact.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByPost(Post post);

    List<Comment> findAllByUser(User user);
}
