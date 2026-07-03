package com.krupa.finsightai.category;

import java.util.List;

import org.springframework.stereotype.Service;

import com.krupa.finsightai.model.User;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getCategories(User user) {
        return categoryRepository.findByUser(user);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}