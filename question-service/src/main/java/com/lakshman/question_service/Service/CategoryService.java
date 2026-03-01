package com.lakshman.question_service.Service;

import com.lakshman.question_service.Utility.ResponseUtil;
import com.lakshman.question_service.Entity.Category;
import com.lakshman.question_service.Exception.ResourceNotFoundException;
import com.lakshman.question_service.Repository.CategoryJpaRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CategoryService {

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Transactional
    public ResponseEntity<?> saveCategory(@Valid Category category) {

        Integer categoryId = categoryJpaRepository.findByCategoryName(category.getCategoryName());

        Map<String, Object> responseData = new HashMap<>();
        // If category does not exist → CREATE
        if (categoryId == null) {

            Category savedCategory = categoryJpaRepository.save(category);

            responseData.put("categoryId", savedCategory.getId());
            responseData.put("categoryName", savedCategory.getCategoryName());

            return ResponseUtil.created(
                    "Category created successfully",
                    responseData
            );
        }

        // If category exists → UPDATE description
        int rowCount = categoryJpaRepository
                .updateDescriptionByCategoryName(categoryId, category.getDescription());

        if (rowCount == 0) {
            throw new ResourceNotFoundException("Category not found");
        }

        responseData.put("categoryId", categoryId);
        responseData.put("categoryName", category.getCategoryName());
        responseData.put("updatedRows", rowCount);

        return ResponseUtil.ok(
                "Category description updated successfully",
                responseData
        );
    }
}