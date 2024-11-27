package com.example.blog.services.impl;

import com.example.blog.dto.PostDto;
import com.example.blog.entity.Category;
import com.example.blog.entity.Post;
import com.example.blog.entity.User;
import com.example.blog.exceptions.ResourceNotFoundException;
import com.example.blog.payload.PostResponse;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PostDto createPost(PostDto postDto, Integer userID, Integer categoryId) {
        User user=this.userRepository.findById(userID).orElseThrow(()-> new ResourceNotFoundException("User","user ID",userID));
        Category category=this.categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","category ID",categoryId));

        Post post=this.modelMapper.map(postDto,Post.class);
        post.setImageName("default.png");
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(category);

        Post createdPost=this.postRepository.save(post);
        return this.modelMapper.map(createdPost,PostDto.class) ;
    }

    @Override
    public String updatePost(PostDto postDto, Integer postId) {
        Post post=this.postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","post ID",postId));

        //only fields provided in request are updated
        if(postDto.getUser()!=null){
            post.setUser(this.modelMapper.map((postDto.getUser()),User.class));
        }
        if(postDto.getCategory()!=null){
            post.setCategory(this.modelMapper.map((postDto.getCategory()),Category.class));
        }
        if(postDto.getTitle()!=null){
            post.setTitle(postDto.getTitle());
        }
        if(postDto.getContent()!=null){
            post.setContent(postDto.getContent());
        }
        if(postDto.getImageName()!=null){
            post.setImageName(postDto.getImageName());
        }
        if(postDto.getAddedDate()!=null){
            post.setAddedDate(postDto.getAddedDate());
        }

        this.postRepository.save(post);
        return "Updated Post Successfully!!";
    }

    @Override
    public String deletePost(Integer postId) {
        Post post=this.postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","post ID",postId));
        this.postRepository.deleteById(postId);
        return "Deleted Post with Id"+postId+" Successfully !!";
    }

    @Override
    public PostResponse getAllPosts(Integer pageNumber, Integer pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending());

        Pageable p= PageRequest.of(pageNumber,pageSize, sort);
        Page<Post> pagePosts=this.postRepository.findAll(p);
        List<Post> allPosts=pagePosts.getContent();

        List<PostDto> postDtos=allPosts.stream().map((post)->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
        PostResponse response=new PostResponse();
        response.setContent(postDtos);
        response.setPageNumber(pagePosts.getNumber());
        response.setPageSize(pagePosts.getSize());
        response.setTotalElements(pagePosts.getTotalElements());
        response.setTotalPages(pagePosts.getTotalPages());
        response.setLastPage(pagePosts.isLast());
        return response;
    }

    @Override
    public PostDto getPostById(Integer postId) {
        Post post=this.postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","post ID",postId));
        return this.modelMapper.map(post,PostDto.class);
    }

    @Override
    public List<PostDto> getAllPostsByUser(Integer userId) {
        User user=this.userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","user ID",userId));
        List<Post> posts=this.postRepository.findByUser(user);
        //convert to PostDto
        List<PostDto> postDtos=posts.stream().map((post)->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public List<PostDto> getAllPostsByCategory(Integer categoryId) {
        Category cat=this.categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","category ID",categoryId));
        List<Post> posts=this.postRepository.findByCategory(cat);

        List<PostDto> postDtos=posts.stream().map((post)->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public List<PostDto> searchPost(String keyword) {
        return null;
    }
}
