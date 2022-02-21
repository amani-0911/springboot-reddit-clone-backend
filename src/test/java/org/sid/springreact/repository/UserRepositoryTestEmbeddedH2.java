package org.sid.springreact.repository;

import org.junit.jupiter.api.Test;
import org.sid.springreact.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTestEmbeddedH2 {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveUser(){
        User user=new User(null,"test user", "secret Password","user@email.com", Instant.now(),true);
        User saveUser=userRepository.save(user);
        assertThat(saveUser).usingRecursiveComparison().ignoringFields("userId").isEqualTo(user);

    }
    @Test
    @Sql("classpath:test-data.sql")
    public void shouldSaveUsersThroughSqlFile(){
        Optional<User> test=userRepository.findByUsername("testuser_sql");
        assertThat(test).isNotEmpty();
    }
}