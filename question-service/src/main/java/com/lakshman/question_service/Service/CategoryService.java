package com.lakshman.question_service.Service;

import com.lakshman.question_service.Entity.Category;
import com.lakshman.question_service.Repository.CategoryJpaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    CategoryJpaRepository categoryJpaRepository;

    public String saveCategory(@Valid Category category) {

        String response = "Not Created";
        try{
            if(category != null){
                categoryJpaRepository.save(category);
                response = "Created";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
