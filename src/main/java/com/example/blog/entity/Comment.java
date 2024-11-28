package com.example.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="comments")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    private String content;
    @ManyToOne
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne              //parent comment can have multiple replies
    @JoinColumn(name="parent_comment_id")
    private Comment parentComment;               //top level comment will be null

    @OneToMany(mappedBy ="parentComment" )
    private List<Comment> replies=new ArrayList<>();

}
