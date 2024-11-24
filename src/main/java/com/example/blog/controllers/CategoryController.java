package com.example.blog.controllers;

import com.example.blog.dto.CategoryDto;
import com.example.blog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //create
    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto){
        CategoryDto createdCategory=this.categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(createdCategory,HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{catId}")
    public ResponseEntity<String> updateCategory(@RequestBody CategoryDto categoryDto ,@PathVariable Integer catId){
        if(categoryDto==null){
            return new ResponseEntity<>("Body cannot be null",HttpStatus.BAD_REQUEST);
        }
        String res=this.categoryService.updateCategory(categoryDto,catId);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{catId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer catId){
        String res=this.categoryService.deleteCategory(catId);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    //get
    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer catId){
        CategoryDto categoryDto=this.categoryService.getCategoryById(catId);
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);
    }

    //getAll
    @GetMapping("/allCategories")
    public List<CategoryDto> getAllCategory(){
        return this.categoryService.getAllCategories();
    }

}
