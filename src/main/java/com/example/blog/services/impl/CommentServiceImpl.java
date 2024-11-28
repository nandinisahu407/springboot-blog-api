package com.example.blog.services.impl;

import com.example.blog.dto.CommentDto;
import com.example.blog.entity.Comment;
import com.example.blog.entity.Post;
import com.example.blog.entity.User;
import com.example.blog.exceptions.ResourceNotFoundException;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId,Integer userId) {
        Post post=this.postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","post id",postId));
        User user= this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","user id",userId));

        Comment comment=this.modelMapper.map(commentDto,Comment.class);
        comment.setPost(post);
        comment.setUser(user);
        Comment savedComment=this.commentRepository.save(comment);
        return this.modelMapper.map(savedComment,CommentDto.class);
    }

    @Override
    public String deleteComment(Integer commentId) {
        Comment comment=this.commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","comment id",commentId));
        this.commentRepository.deleteById(commentId);
        return "Deleted comment Successfully !";
    }

    @Override
    public String updateComment(CommentDto commentDto,Integer commentId, Integer postId, Integer userId) {
        Post post=this.postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","post id",postId));
        User user= this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","user id",userId));
        Comment comment=this.commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment","comment id",commentId));

        //allow edit by owner only
        if(user!=comment.getUser()){
            return "Edit failed :( Comments can be updated only by the owner !!";
        }
        comment.setContent(commentDto.getContent());
        this.commentRepository.save(comment);
        return "Edited comment successfully";
    }

    @Override
    public List<CommentDto> getAllComments(Integer postId) {
        Post post=this.postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","post id",postId));
        List<Comment> comments = commentRepository.findCommentByPostId(postId);

        return comments.stream()
                .map(comment -> {
                    // Create the CommentDto for the top-level comment
                    CommentDto commentDto = new CommentDto(
                            comment.getCommentId(),
                            comment.getContent(),
                            comment.getUser().getUserName() // Fetch user's name
                    );

                    //  map its replies
                    comment.getReplies().forEach(reply -> {
                        // Map each reply recursively
                        CommentDto replyDto = new CommentDto(
                                reply.getCommentId(),
                                reply.getContent(),
                                reply.getUser().getUserName() // Fetch user's name for reply
                        );
                        commentDto.addReply(replyDto); // Add the reply to the current comment
                    });

                    return commentDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto createReplyToComment(Integer postId, Integer parentCommentId, Integer userId, CommentDto replyComment) {
        Post post=this.postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","post id",postId));
        Comment parentComment=this.commentRepository.findById(parentCommentId).orElseThrow(()->new ResourceNotFoundException("Comment","comment id",parentCommentId));
        User user= this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","user id",userId));

        Comment reply=this.modelMapper.map(replyComment,Comment.class);
        reply.setUser(user);
        reply.setPost(post);
        reply.setParentComment(parentComment);

        //parent comment list-> update karo with new reply
        parentComment.getReplies().add(reply);
        commentRepository.save(reply);
        commentRepository.save(parentComment);

        CommentDto dto=new CommentDto(reply.getCommentId(),reply.getContent(),user.getUserName());
        return dto;
    }


}
