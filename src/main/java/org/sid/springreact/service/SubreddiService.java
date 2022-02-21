package org.sid.springreact.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.springreact.dto.SubredditDto;
import org.sid.springreact.entities.Subreddit;
import org.sid.springreact.exceptions.SpringRedditException;
import org.sid.springreact.mapper.SubredditMapper;
import org.sid.springreact.repository.SubredditRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubreddiService {
    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;
    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
      Subreddit subredditsave=subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
      subredditDto.setId(subredditsave.getId());
      return subredditDto;
    }

 @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }


    public SubredditDto getSubreddit(Long id) {
   Subreddit subreddit=subredditRepository.findById(id).orElseThrow(()->new SpringRedditException("No subreddit found with ID - " + id));
   return subredditMapper.mapSubredditToDto(subreddit);
    }
}
