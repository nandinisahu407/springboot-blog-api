package com.example.blog.services;
import com.example.blog.dto.CategoryDto;
import java.util.List;

public interface CategoryService {
    //create
     CategoryDto createCategory(CategoryDto categoryDto);
     //update
    String updateCategory(CategoryDto categoryDto, Integer categoryId);
    //delete
    String deleteCategory(Integer categoryId);
    //get
    CategoryDto getCategoryById(Integer categoryId);
    //getAll
    List<CategoryDto> getAllCategories();

}
