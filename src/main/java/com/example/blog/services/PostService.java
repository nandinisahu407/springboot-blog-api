package com.example.blog.services;

import com.example.blog.dto.PostDto;
import com.example.blog.entity.Post;
import com.example.blog.entity.User;

import java.util.List;

public interface PostService {
    //crud
    Post createPost(PostDto postDto,Integer userID,Integer categoryId) ;

    String updatePost(PostDto postDto, Integer postId);
    String deletePost(Integer postId);
    List<Post> getAllPosts();
    Post getPostById(Integer postId);

    //get all posts by user, category
    List<Post> getAllPostsByUser(Integer userId);
    List<Post> getAllPostsByCategory(Integer categoryId);

    //search posts by keyword
    List<Post> searchPost(String keyword);

}
