package com.example.blog.controllers;

import com.example.blog.dto.PostDto;
import com.example.blog.entity.Post;
import com.example.blog.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private ModelMapper modelMapper;

    //create
    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer userId, @PathVariable Integer categoryId){
        PostDto savedPost=this.postService.createPost(postDto,userId,categoryId);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    //get posts by user
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<PostDto>>  getAllpostByUser(@PathVariable Integer userId){
        List<PostDto> resp=this.postService.getAllPostsByUser(userId);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    //get posts by category
    @GetMapping("/category/{categoryId}/posts")
    public ResponseEntity<List<PostDto>> getAllpostByCategory(@PathVariable Integer categoryId){
        List<PostDto> resp= this.postService.getAllPostsByCategory(categoryId);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    //update
    @PutMapping("/post/{postId}")
    public ResponseEntity<String> updatePost(@RequestBody PostDto updatedPost, @PathVariable Integer postId){
        String resp=this.postService.updatePost(updatedPost,postId);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Integer postId){
        String resp=this.postService.deletePost(postId);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    //get  post by id
    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDto> getPostByID(@PathVariable Integer postId){
        PostDto post=this.postService.getPostById(postId);
        return new ResponseEntity<>(post,HttpStatus.OK);
    }

    //all posts
    @GetMapping("/allpost")
    public ResponseEntity<List<PostDto>> getAllPosts(){
        List<PostDto> post=this.postService.getAllPosts();
        return new ResponseEntity<>(post,HttpStatus.OK);
    }



}
