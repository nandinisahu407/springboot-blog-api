package com.example.blog.controllers;

import com.example.blog.dto.CommentDto;
import com.example.blog.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/post/{postId}/user/{userId}/comment")
    public ResponseEntity<CommentDto> createCOmment(@RequestBody CommentDto commentDto, @PathVariable Integer postId, @PathVariable Integer userId ){
        CommentDto response=this.commentService.createComment(commentDto,postId,userId);
        return  new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Integer commentId ){
        String response=this.commentService.deleteComment(commentId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("/post/{postId}/user/{userId}/comment/{commentId}")
    public ResponseEntity<String> editComments(
            @RequestBody CommentDto commentDto,
            @PathVariable Integer postId,
            @PathVariable Integer userId,
            @PathVariable Integer commentId
    ){
        if(commentDto.getContent()==null){
            return new ResponseEntity<>("content cannot be null",HttpStatus.BAD_REQUEST);
        }

        String response=this.commentService.updateComment(commentDto,commentId,postId,userId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    //show all comments for a post
    @GetMapping("/post/{postId}/allComments")
    public ResponseEntity<List<CommentDto>> getAllCommentsForPost(@PathVariable Integer postId){
            List<CommentDto> response=this.commentService.getAllComments(postId);
            return new ResponseEntity<>(response,HttpStatus.OK);
    }

    //reply to comment
    @PostMapping("/post/{postId}/comment/{parentCommentId}/replies")
    public ResponseEntity<CommentDto> createReply(
            @PathVariable Integer postId,
            @PathVariable Integer parentCommentId,
            @RequestParam Integer userId,
            @RequestBody CommentDto commentDto) {

        CommentDto reply = this.commentService.createReplyToComment(postId,parentCommentId,userId,commentDto);
        return new ResponseEntity<>(reply, HttpStatus.CREATED);
    }

}
