package org.sid.springreact.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sid.springreact.dto.PostRequest;
import org.sid.springreact.dto.PostResponse;
import org.sid.springreact.entities.Post;
import org.sid.springreact.entities.Subreddit;
import org.sid.springreact.entities.User;
import org.sid.springreact.mapper.PostMapper;
import org.sid.springreact.repository.PostRepository;
import org.sid.springreact.repository.SubredditRepository;
import org.sid.springreact.repository.UserRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    //creerr les instance Mocketo
   //  private  PostRepository postRepository= Mockito.mock(PostRepository.class);
   // private SubredditRepository subredditRepository= Mockito.mock(SubredditRepository.class);
   // private  AuthService authService= Mockito.mock(AuthService.class);
   // private  PostMapper postMapper= Mockito.mock(PostMapper.class);
    //private  UserRepository userRepository= Mockito.mock(UserRepository.class);;
    @Mock
    private  PostRepository postRepository;
    @Mock
    private SubredditRepository subredditRepository;
    @Mock
     private  AuthService authService;
    @Mock
    private  PostMapper postMapper;
    @Mock
    private  UserRepository userRepository;

    @Captor
    private ArgumentCaptor<Post> postArgumentCaptor;

    PostService postService;
    @BeforeEach
    public void setup(){
         postService=new PostService(postRepository,subredditRepository,authService,postMapper,userRepository);

    }

    @Test
    @DisplayName("Should Find Post by Id")
    void shouldFindPostById() {
     Post post = new Post(123L, "First Post", "http://url.site", "Test",
                0, null, Instant.now(), null);
        Mockito.when(postRepository.findById(123L)).thenReturn(Optional.of(post));

        PostResponse expectedPostResponse = new PostResponse(123L, "First Post", "http://url.site", "Test",
                "Test User", "Test Subredit", 0, 0, "1 Hour Ago", false, false);

    Mockito.when(postMapper.mapToDto(Mockito.any(Post.class))).thenReturn(expectedPostResponse);

    PostResponse actualPostResponse=postService.getPost(123L);

    Assertions.assertThat(actualPostResponse.getId()).isEqualTo(expectedPostResponse.getId());
    Assertions.assertThat(actualPostResponse.getPostName()).isEqualTo(expectedPostResponse.getPostName());
    }

    @Test
    @DisplayName("Should display All Posts")
    public void shouldDisplayAllPosts() {
        Post post = new Post(123L, "First Post", "http://url.site", "Test",
                0, null, Instant.now(), null);
   Mockito.when(postRepository.findAll()).thenReturn(Arrays.asList(post));
        PostResponse expectedPostResponse = new PostResponse(123L, "First Post", "http://url.site", "Test",
                "Test User", "Test Subredit", 0, 0, "1 Hour Ago", false, false);

        Mockito.when(postMapper.mapToDto(Mockito.any(Post.class))).thenReturn(expectedPostResponse);
        List<PostResponse> actualPosts=postService.getAllPosts();


        Assertions.assertThat(actualPosts.get(0).getId()).isEqualTo(expectedPostResponse.getId());
        Assertions.assertThat(actualPosts.get(0).getPostName()).isEqualTo(expectedPostResponse.getPostName());


    }


    @Test
    @DisplayName("Should Save Posts")
    public void shouldSavePosts() {

        User currentUser = new User(123L, "test user", "secret password", "user@email.com", Instant.now(), true);
        Subreddit subreddit = new Subreddit(123L, "First Subreddit", "Subreddit Description", Collections.emptyList(), Instant.now(), currentUser);
        Post post = new Post(123L, "First Post", "http://url.site", "Test",
                0, null, Instant.now(), null);
        PostRequest postRequest = new PostRequest(null, "First Subreddit", "First Post", "http://url.site", "Test");

        Mockito.when(subredditRepository.findByName("First Subreddit"))
                .thenReturn(Optional.of(subreddit));
        Mockito.when(authService.getCurrentUser())
                .thenReturn(currentUser);
        Mockito.when(postMapper.map(postRequest, subreddit, currentUser))
                .thenReturn(post);

        postService.save(postRequest);
        Mockito.verify(postRepository, Mockito.times(1)).save(postArgumentCaptor.capture());

        Assertions.assertThat(postArgumentCaptor.getValue().getPostId()).isEqualTo(123L);
        Assertions.assertThat(postArgumentCaptor.getValue().getPostName()).isEqualTo("First Post");



    }

}