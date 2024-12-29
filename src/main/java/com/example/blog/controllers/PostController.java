package com.example.blog.controllers;

import com.example.blog.config.SecurityUtils;
import com.example.blog.constants.EntityType;
import com.example.blog.dto.PostDto;
import com.example.blog.entity.Post;
import com.example.blog.entity.User;
import com.example.blog.exceptions.ResourceNotFoundException;
import com.example.blog.payload.PostResponse;
import com.example.blog.repository.PostRepository;
import com.example.blog.services.FileService;
import com.example.blog.services.LogEntryService;
import com.example.blog.services.PostService;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LogEntryService logEntryService;
    @Value("${project.image}")
    private String path;

    //create
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or hasRole('NORMAL')")
    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer userId, @PathVariable Integer categoryId){
        PostDto savedPost=this.postService.createPost(postDto,userId,categoryId);

        //adding logs
        User loggedInUser = securityUtils.getLoggedInUserDetails();
        logEntryService.logAction(
                loggedInUser.getname(),
                "CREATED",
                EntityType.POST,
                postDto.getUser().getUserName(),
                "Post created with title:"+postDto.getTitle()
        );
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
        //normal user can update only their own profiles
        User loggedInUser = securityUtils.getLoggedInUserDetails();
        boolean isNormalUser = loggedInUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_NORMAL"));
        Post postTOBeUpdated=this.postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","post ID ",postId));
        Integer postOwnerId = postTOBeUpdated.getUser().getId();
        if ((isNormalUser && (loggedInUser.getId()!=postOwnerId))
                && !securityUtils.hasRole("ROLE_ADMIN")
                && !securityUtils.hasRole("ROLE_SUPER_ADMIN")
        ) {
            return new ResponseEntity<>("Normal users can only update their own profile.", HttpStatus.FORBIDDEN);
        }

        //admin,super-admin,normal(owner)
        String resp=this.postService.updatePost(updatedPost,postId);
        //adding logs
        logEntryService.logAction(
                loggedInUser.getname(),
                "UPDATED",
                EntityType.POST,
                updatedPost.getUser().getUserName(),
                "Post updated with title:"+updatedPost.getTitle()
        );
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Integer postId){
        //normal user-> can delete its own post
        User loggedInUser = securityUtils.getLoggedInUserDetails();
        boolean isNormalUser = loggedInUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_NORMAL"));
        Post postTOBeDeleted=this.postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","post ID ",postId));
        Integer postOwnerId = postTOBeDeleted.getUser().getId();
        if ((isNormalUser && (loggedInUser.getId()!=postOwnerId))
                && !securityUtils.hasRole("ROLE_ADMIN")
                && !securityUtils.hasRole("ROLE_SUPER_ADMIN")
        ) {
            return new ResponseEntity<>("Normal users can only delete their own profile.", HttpStatus.FORBIDDEN);
        }

        //admin,super-admin,normal(owner)
        String resp=this.postService.deletePost(postId);
        //adding logs
        logEntryService.logAction(
                loggedInUser.getname(),
                "DELETED",
                EntityType.POST,
                postTOBeDeleted.getUser().getname(),
                "Post deleted with title:"+postTOBeDeleted.getTitle()
        );
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
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value="pageNumber",defaultValue = "0",required=false) Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) Integer pageSize,
            @RequestParam(value = "sortBy",defaultValue = "postId",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PostResponse response=this.postService.getAllPosts(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/posts/search/{keyword}")
    public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable String keyword){
        List<PostDto> response= this.postService.searchPost(keyword);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    //post image upload
    @PostMapping("/post/image/upload/{postId}")
    public ResponseEntity<String> uploadPostImage(
            @PathVariable Integer postId,
            @RequestParam("image")MultipartFile image
    ) throws IOException {
        PostDto post=this.postService.getPostById(postId);

        String fileName=this.fileService.uploadImage(path,image);
        post.setImageName(fileName);
        String response=this.postService.updatePost(post,postId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    //method to serve file
    @GetMapping(value="/post/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<String> downloadFile(
            @PathVariable("imageName") String imageName,
            HttpServletResponse response
    ) throws IOException {
        try {
            InputStream resource= this.fileService.getResource(path,imageName);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            StreamUtils.copy(resource,response.getOutputStream());
            return new ResponseEntity<>("downloaded Successfully",HttpStatus.OK);
        }catch(FileNotFoundException ex){
            return new ResponseEntity<>("image not found-> "+ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }



}
