package com.govind.ecommerce.service.category;

import com.govind.ecommerce.model.Category;

import java.util.List;

public interface ICategoryService {

    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    List<Category> getAllCategories();

    Category addCategory(Category category);

    Category updateCategory(Category category, Long categoryId);

    void deleteCategoryById(Long id);

}
