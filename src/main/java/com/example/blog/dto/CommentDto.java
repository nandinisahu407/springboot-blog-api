package com.example.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Integer commentId;
    private String content;
    private String userName;
    private List<CommentDto> replies = new ArrayList<>();

    public CommentDto(Integer commentId, String content, String userName) {
        this.commentId = commentId;
        this.content = content;
        this.userName = userName;
    }

    public void addReply(CommentDto reply) {
        this.replies.add(reply);
    }
}
