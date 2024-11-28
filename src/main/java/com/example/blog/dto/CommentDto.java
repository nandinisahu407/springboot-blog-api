package com.example.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private Integer commentId;
    private String content;
    private String userName;

    public CommentDto(Integer commentId, String content, String userName) {
        this.commentId = commentId;
        this.content = content;
        this.userName = userName;
    }
}
