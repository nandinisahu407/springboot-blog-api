package com.example.blog.services;

import com.example.blog.dto.PostDto;
import com.example.blog.entity.Post;
import com.example.blog.entity.User;
import com.example.blog.payload.PostResponse;

import java.util.List;

public interface PostService {
    //crud
    PostDto createPost(PostDto postDto,Integer userID,Integer categoryId) ;

    String updatePost(PostDto postDto, Integer postId);
    String deletePost(Integer postId);
    PostResponse getAllPosts(Integer pageNumber, Integer pageSize,String sortBy,String sortDir);
    PostDto getPostById(Integer postId);

    //get all posts by user, category
    List<PostDto> getAllPostsByUser(Integer userId);
    List<PostDto> getAllPostsByCategory(Integer categoryId);

    //search posts by keyword
    List<PostDto> searchPost(String keyword);

}
