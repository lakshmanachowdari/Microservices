package com.lakshman.question_service.Controller;

import com.lakshman.question_service.Constants.MarketDataConstants;
import com.lakshman.question_service.Entity.Category;
import com.lakshman.question_service.Service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(value = MarketDataConstants.ADD_CATEGORY, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCategory(@Valid @RequestBody Category category) {
        return categoryService.saveCategory(category);
    }

    @GetMapping(value = MarketDataConstants.GET_CATEGORIES, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCategories() {
        return categoryService.getCategories();
    }

}
