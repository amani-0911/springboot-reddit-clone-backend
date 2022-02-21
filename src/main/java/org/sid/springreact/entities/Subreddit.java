package org.sid.springreact.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subreddit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "community name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "subreddit")
    private List<Post> posts;
    private Instant createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;


}
