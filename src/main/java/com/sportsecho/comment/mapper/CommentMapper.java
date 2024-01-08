package com.sportsecho.comment.mapper;

import com.sportsecho.comment.dto.CommentRequestDto;
import com.sportsecho.comment.dto.CommentResponseDto;
import com.sportsecho.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "member.memberName", target = "memberName")
    CommentResponseDto commentToCommentResponseDto(Comment comment);

    Comment commentRequestDtoToComment(CommentRequestDto dto);
}
