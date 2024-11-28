package com.example.blog.services;

import com.example.blog.dto.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(CommentDto commentDto,Integer postId,Integer userId);

    String deleteComment(Integer commentId);

    String updateComment(CommentDto commentDto,Integer commentId,Integer postId,Integer userId);

    List<CommentDto> getAllComments(Integer postId);
}
