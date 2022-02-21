package org.sid.springreact.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.springreact.dto.VoteDto;
import org.sid.springreact.entities.Post;
import org.sid.springreact.entities.Vote;
import org.sid.springreact.entities.VoteType;
import org.sid.springreact.exceptions.PostNotFoundException;
import org.sid.springreact.exceptions.SpringRedditException;
import org.sid.springreact.mapper.VoteMapper;
import org.sid.springreact.repository.PostRepository;
import org.sid.springreact.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class VoteService {
    private final AuthService authService;
    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final VoteMapper voteMapper;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post=postRepository.findById(voteDto.getPostId())
                .orElseThrow(()->new PostNotFoundException("Post Not Found with ID - "+voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser=voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,authService.getCurrentUser());
   if(voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())){
       throw new SpringRedditException("You have already"+voteDto.getVoteType()+"'d for this post");
   }
   if (VoteType.UPVOTE.equals(voteDto.getVoteType())){
       post.setVoteCount(post.getVoteCount()+1);
   }else{
       post.setVoteCount(post.getVoteCount()-1);
   }
   voteRepository.save(voteMapper.mapToVote(voteDto,post,authService.getCurrentUser()));
   postRepository.save(post);
    }
}
