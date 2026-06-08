package com.lakshman.question_service.Service;

import com.lakshman.question_service.Entity.Category;
import com.lakshman.question_service.Exception.ResourceNotFoundException;
import com.lakshman.question_service.Repository.CategoryJpaRepository;
import com.lakshman.question_service.Utility.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryService Unit Tests")
class CategoryServiceTest {

    @Mock
    private CategoryJpaRepository categoryJpaRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category newCategory;
    private Category existingCategory;

    @BeforeEach
    void setUp() {
        newCategory = new Category();
        newCategory.setCategoryName("NewCat");
        newCategory.setDescription("New description");

        existingCategory = new Category();
        existingCategory.setCategoryName("ExistingCat");
        existingCategory.setDescription("Updated description");
    }

    @Test
    @DisplayName("saveCategory - creates a new category when it does not exist")
    void saveCategory_createsNewCategory() {
        // repository returns null => category does not exist
        when(categoryJpaRepository.findByCategoryName(newCategory.getCategoryName())).thenReturn(null);

        // simulate saved entity with id set
        Category saved = new Category();
        saved.setCategoryName(newCategory.getCategoryName());
        saved.setDescription(newCategory.getDescription());
        saved.setId(42);

        when(categoryJpaRepository.save(any(Category.class))).thenReturn(saved);

        ResponseEntity<?> resp = categoryService.saveCategory(newCategory);

        assertNotNull(resp);
        assertEquals(201, resp.getStatusCodeValue());

        // Response body is ApiResponse<Map<String,Object>>
        @SuppressWarnings("unchecked")
        ApiResponse<Map<String, Object>> body = (ApiResponse<Map<String, Object>>) resp.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
        assertNotNull(body.getData());
        Map<String, Object> data = body.getData();
        assertEquals(42, ((Number) data.get("categoryId")).intValue());
        assertEquals(newCategory.getCategoryName(), data.get("categoryName"));

        verify(categoryJpaRepository, times(1)).findByCategoryName(newCategory.getCategoryName());
        verify(categoryJpaRepository, times(1)).save(any(Category.class));
        verifyNoMoreInteractions(categoryJpaRepository);
    }

    @Test
    @DisplayName("saveCategory - updates description when category exists")
    void saveCategory_updatesDescriptionWhenExists() {
        Integer existingId = 7;
        when(categoryJpaRepository.findByCategoryName(existingCategory.getCategoryName())).thenReturn(existingId);

        // update returns 1 row updated
        when(categoryJpaRepository.updateDescriptionByCategoryName(existingId, existingCategory.getDescription()))
                .thenReturn(1);

        ResponseEntity<?> resp = categoryService.saveCategory(existingCategory);

        assertNotNull(resp);
        assertEquals(200, resp.getStatusCodeValue());

        @SuppressWarnings("unchecked")
        ApiResponse<Map<String, Object>> body = (ApiResponse<Map<String, Object>>) resp.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
        Map<String, Object> data = body.getData();
        assertEquals(existingId.intValue(), ((Number) data.get("categoryId")).intValue());
        assertEquals(existingCategory.getCategoryName(), data.get("categoryName"));
        assertEquals(1, ((Number) data.get("updatedRows")).intValue());

        verify(categoryJpaRepository, times(1)).findByCategoryName(existingCategory.getCategoryName());
        verify(categoryJpaRepository, times(1)).updateDescriptionByCategoryName(existingId, existingCategory.getDescription());
        verifyNoMoreInteractions(categoryJpaRepository);
    }

    @Test
    @DisplayName("saveCategory - throws ResourceNotFoundException when update affects 0 rows")
    void saveCategory_updateReturnsZero_throws() {
        Integer existingId = 10;
        when(categoryJpaRepository.findByCategoryName(existingCategory.getCategoryName())).thenReturn(existingId);

        // update returns 0 -> should throw ResourceNotFoundException
        when(categoryJpaRepository.updateDescriptionByCategoryName(existingId, existingCategory.getDescription()))
                .thenReturn(0);

        assertThrows(ResourceNotFoundException.class, () -> categoryService.saveCategory(existingCategory));

        verify(categoryJpaRepository, times(1)).findByCategoryName(existingCategory.getCategoryName());
        verify(categoryJpaRepository, times(1)).updateDescriptionByCategoryName(existingId, existingCategory.getDescription());
        verifyNoMoreInteractions(categoryJpaRepository);
    }

    @Test
    @DisplayName("getCategories - returns all categories")
    void getCategories_returnsAll() {
        Category c1 = new Category();
        c1.setId(1);
        c1.setCategoryName("C1");
        c1.setDescription("D1");

        Category c2 = new Category();
        c2.setId(2);
        c2.setCategoryName("C2");
        c2.setDescription("D2");

        List<Category> list = Arrays.asList(c1, c2);
        when(categoryJpaRepository.findAll()).thenReturn(list);

        ResponseEntity<?> resp = categoryService.getCategories();

        assertNotNull(resp);
        assertEquals(200, resp.getStatusCodeValue());

        @SuppressWarnings("unchecked")
        ApiResponse<List<Category>> body = (ApiResponse<List<Category>>) resp.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
        List<Category> data = body.getData();
        assertNotNull(data);
        assertEquals(2, data.size());
        assertEquals("No of categories fetched: 2", body.getMessage());

        verify(categoryJpaRepository, times(1)).findAll();
        verifyNoMoreInteractions(categoryJpaRepository);
    }
}