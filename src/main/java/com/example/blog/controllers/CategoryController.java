package com.example.blog.controllers;

import com.example.blog.config.SecurityUtils;
import com.example.blog.constants.EntityType;
import com.example.blog.dto.CategoryDto;
import com.example.blog.entity.Category;
import com.example.blog.entity.User;
import com.example.blog.services.CategoryService;
import com.example.blog.services.LogEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private LogEntryService logEntryService;
    @Autowired
    private SecurityUtils securityUtils;

    //create
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or hasRole('NORMAL')")
    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto){
        CategoryDto createdCategory=this.categoryService.createCategory(categoryDto);
        //adding logs
        User loggedInUser = securityUtils.getLoggedInUserDetails();
        logEntryService.logAction(
                loggedInUser.getname(),
                "CREATED",
                EntityType.CATEGORY,
                "Id: "+createdCategory.getCategoryId(),
                "Category created with title:"+createdCategory.getCategoryTitle()
        );
        return new ResponseEntity<>(createdCategory,HttpStatus.CREATED);
    }

    //update
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or hasRole('NORMAL')")
    @PutMapping("/{catId}")
    public ResponseEntity<String> updateCategory(@RequestBody CategoryDto categoryDto ,@PathVariable Integer catId){
        if(categoryDto==null){
            return new ResponseEntity<>("Body cannot be null",HttpStatus.BAD_REQUEST);
        }
        String res=this.categoryService.updateCategory(categoryDto,catId);
        //add activity logs
        User loggedInUser = securityUtils.getLoggedInUserDetails();
        logEntryService.logAction(
                loggedInUser.getname(),
                "UPDATED",
                EntityType.CATEGORY,
                "Id: "+categoryDto.getCategoryId(),
                "Category Updated with title:"+categoryDto.getCategoryTitle()
        );
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    //delete
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or hasRole('NORMAL')")
    @DeleteMapping("/{catId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer catId){
        String res=this.categoryService.deleteCategory(catId);
        //add activity logs
        User loggedInUser = securityUtils.getLoggedInUserDetails();
        logEntryService.logAction(
                loggedInUser.getname(),
                "DELETED",
                EntityType.CATEGORY,
                "Id: "+catId,
                "Category deleted with Id:"+catId
        );
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
