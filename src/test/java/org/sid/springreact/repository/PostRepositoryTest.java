package org.sid.springreact.repository;


import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.sid.springreact.BaseTest;
import org.sid.springreact.entities.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest extends BaseTest {

  @Autowired
  private PostRepository postRepository;
    @Test
    public void shouldSavePost() {
        Post expectedPostObject = new Post(null, "First Post", "http://url.site", "Test",
                0, null, Instant.now(), null);
        Post actualPostObject = postRepository.save(expectedPostObject);
        Assertions.assertThat(actualPostObject).usingRecursiveComparison()
                .ignoringFields("postId").isEqualTo(expectedPostObject);
    }

}