package com.example.blog.repository;

import com.example.blog.entity.Comment;
import com.example.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {

    //fetch comments by postId
    @Query("select c from Comment c where c.post.postId = :postId")
    List<Comment> findCommentByPostId(@Param("postId") Integer postId);

}
