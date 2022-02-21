package org.sid.springreact.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.springreact.dto.CommentsDto;
import org.sid.springreact.entities.Comment;
import org.sid.springreact.entities.NotificationEmail;
import org.sid.springreact.entities.Post;
import org.sid.springreact.entities.User;
import org.sid.springreact.exceptions.PostNotFoundException;
import org.sid.springreact.exceptions.SpringRedditException;
import org.sid.springreact.mapper.CommentMapper;
import org.sid.springreact.repository.CommentRepository;
import org.sid.springreact.repository.PostRepository;
import org.sid.springreact.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private final UserRepository userRepository;


    public void save(CommentsDto commentsDto){
        Post post=postRepository.findById(commentsDto.getPostId()).orElseThrow(
                ()->new PostNotFoundException(commentsDto.getPostId().toString())
        );
        Comment comment=commentMapper.map(commentsDto,post,authService.getCurrentUser());
       commentRepository.save(comment);
        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());

    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));

    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
    Post post=postRepository.findById(postId).orElseThrow(
            ()->new PostNotFoundException(postId.toString())
    );
        List<Comment> comments=commentRepository.findAllByPost(post);
        return comments.stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
    User user=userRepository.findByUsername(userName).orElseThrow(
            ()->new UsernameNotFoundException(userName)
    );
    List<Comment> comments=commentRepository.findAllByUser(user);
    return comments.stream()
            .map(commentMapper::mapToDto)
            .collect(Collectors.toList());
    }
    public boolean containsSwearWords(String comment) {
        if (comment.contains("shit")) {
            throw new SpringRedditException("Comments contains unacceptable language");
        }
        return false;
    }
}
