package org.sid.springreact.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.springreact.dto.PostRequest;
import org.sid.springreact.dto.PostResponse;
import org.sid.springreact.entities.Post;
import org.sid.springreact.entities.Subreddit;
import org.sid.springreact.entities.User;
import org.sid.springreact.exceptions.PostNotFoundException;
import org.sid.springreact.exceptions.SpringRedditException;
import org.sid.springreact.exceptions.SubredditNotFoundException;
import org.sid.springreact.exceptions.UserNotFoundException;
import org.sid.springreact.mapper.PostMapper;
import org.sid.springreact.repository.PostRepository;
import org.sid.springreact.repository.SubredditRepository;
import org.sid.springreact.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    public Post save(PostRequest postRequest) {
     Subreddit subreddit= subredditRepository.findByName(postRequest.getSubredditName()).orElseThrow(
             ()->new SpringRedditException(postRequest.getSubredditName()+" Not Found")
     );
     User currentUser=authService.getCurrentUser();
     return postRepository.save(postMapper.map(postRequest,subreddit,currentUser));

    }
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id){
        Post post=postRepository.findById(id).orElseThrow(
                ()->new PostNotFoundException(id.toString())
        );
        return postMapper.mapToDto(post);
    }
   @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(){
        return postRepository
                .findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
   }
   @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId){
        Subreddit subreddit=subredditRepository.findById(subredditId)
                .orElseThrow(()->new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts=postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
   }
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
    User user=userRepository.findByUsername(username)
            .orElseThrow(()->new UserNotFoundException(username));
   List<Post> posts=postRepository.findAllByUser(user);
   return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }
}
