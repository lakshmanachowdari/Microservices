package com.lakshman.question_service.Controller;

import com.lakshman.question_service.Entity.Category;
import com.lakshman.question_service.Service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("Category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category){
        return new ResponseEntity<>(categoryService.saveCategory(category), HttpStatus.CREATED);
    }
}
