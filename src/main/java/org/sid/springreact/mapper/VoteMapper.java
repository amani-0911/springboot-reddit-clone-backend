package org.sid.springreact.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.sid.springreact.dto.VoteDto;
import org.sid.springreact.entities.Post;
import org.sid.springreact.entities.User;
import org.sid.springreact.entities.Vote;

@Mapper(componentModel = "spring")
public interface VoteMapper {
    @Mapping(target = "voteType",source = "voteDto.voteType")
    @Mapping(target="post",source="post")
    @Mapping(target = "user", source = "user")
    Vote mapToVote(VoteDto voteDto, Post post, User user);
}
