package com.project.shopApp.services.category;

import com.project.shopApp.dtos.CategoryDTO;
import com.project.shopApp.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO category);
    Category getCategoryById(long id);
    List<Category> getAllCategories();
    Category updateCategory(long categoryId, CategoryDTO category);
    void deleteCategory(long id);
}
