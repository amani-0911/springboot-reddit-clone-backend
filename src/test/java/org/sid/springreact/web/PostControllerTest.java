package org.sid.springreact.web;


import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sid.springreact.config.SecurityConfig;
import org.sid.springreact.dto.PostResponse;
import org.sid.springreact.security.JWTProvider;
import org.sid.springreact.service.PostService;
import org.sid.springreact.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static java.util.Arrays.asList;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = PostController.class)
public class PostControllerTest {
    @MockBean
    private PostService postService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private JWTProvider jwtProvider;

    @Autowired
    private MockMvc mockMvc;


    @Test
  public  void shouldNotNull(){
        assertNotNull(mockMvc);
    }


    @Test
    @DisplayName("Should List All Posts When making GET request to endpoint - /api/posts/")
   public void shouldCreatePost() throws Exception {

        PostResponse postRequest1 = new PostResponse(1L, "Post Name", "http://url.site", "Description", "User 1",
                "Subreddit Name", 0, 0, "1 day ago", false, false);
        PostResponse postRequest2 = new PostResponse(2L, "Post Name 2", "http://url2.site2", "Description2", "User 2",
                "Subreddit Name 2", 0, 0, "2 days ago", false, false);

        Mockito.when(postService.getAllPosts()).thenReturn(asList(postRequest1, postRequest2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].postName", Matchers.is("Post Name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].url", Matchers.is("http://url.site")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].url", Matchers.is("http://url2.site2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].postName", Matchers.is("Post Name 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/"))
                .andExpect(MockMvcResultMatchers.status().is(200));

    }

    }