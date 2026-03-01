package com.lakshman.question_service.Repository;

import com.lakshman.question_service.Entity.Category;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Integer> {
    @Query(value = "select id from categories where category = :categoryName;", nativeQuery = true)
    Integer findByCategoryName(@NotBlank(message = "Category Name cannot be empty") String categoryName);

    @Modifying
    @Transactional
    @Query(value = "update categories set description = :description where id = :categoryId", nativeQuery = true)
    Integer updateDescriptionByCategoryName(@Param("categoryId") Integer categoryId,
                                         @Param("description") String description);
}
