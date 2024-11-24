package com.example.blog.services.impl;

import com.example.blog.dto.CategoryDto;
import com.example.blog.entity.Category;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category=this.modelMapper.map(categoryDto, Category.class);
        Category savedCategory=this.categoryRepository.save(category);
        return this.modelMapper.map(savedCategory,CategoryDto.class);
    }

    @Override
    public String updateCategory(CategoryDto categoryDto, Integer categoryId) {
        Category cat=this.categoryRepository.findById(categoryId).orElseThrow(()-> new RuntimeException("category not found"));
        cat.setCategoryTitle(categoryDto.getCategoryTitle());
        cat.setCategoryDescription(categoryDto.getCategoryDescription());
        this.categoryRepository.save(cat);
        return "Updated Successfully";
    }

    @Override
    public String deleteCategory(Integer categoryId) {
        Category cat=this.categoryRepository.findById(categoryId).orElseThrow(()-> new RuntimeException("category not found"));
        this.categoryRepository.deleteById(categoryId);
        return "Deleted Successfully";
    }

    @Override
    public CategoryDto getCategoryById(Integer categoryId) {
        Category cat=this.categoryRepository.findById(categoryId).orElseThrow(()-> new RuntimeException("category not found"));
        return this.modelMapper.map(cat,CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories=this.categoryRepository.findAll();
        return categories.stream()
                .map((cat)-> this.modelMapper.map(cat,CategoryDto.class))
                .collect(Collectors.toList());
    }
}
