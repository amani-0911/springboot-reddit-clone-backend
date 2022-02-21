package org.sid.springreact.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.springreact.dto.SubredditDto;
import org.sid.springreact.service.SubreddiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {

    private final SubreddiService subredditService;
    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto){
     return   ResponseEntity.status(HttpStatus.CREATED)
                .body(subredditService.save(subredditDto));
    }
    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddits(){
     return  ResponseEntity.status(HttpStatus.OK).body(subredditService.getAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(subredditService.getSubreddit(id));
    }
}
