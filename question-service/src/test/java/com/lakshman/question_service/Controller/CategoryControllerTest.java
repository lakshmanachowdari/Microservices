package com.lakshman.question_service.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lakshman.question_service.Entity.Category;
import com.lakshman.question_service.Service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@DisplayName("CategoryController MVC tests")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private Category sampleCategory;
    private List<Category> categoryList;

    @BeforeEach
    void setUp() {
        sampleCategory = new Category();
        sampleCategory.setId(1);
        sampleCategory.setCategoryName("Java");
        sampleCategory.setDescription("Java Programming Language");

        Category category2 = new Category();
        category2.setId(2);
        category2.setCategoryName("Python");
        category2.setDescription("Python Programming Language");

        categoryList = Arrays.asList(sampleCategory, category2);
    }

    @Test
    @DisplayName("Should create category successfully")
    void createCategory_success() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Category created successfully");
        response.put("categoryId", 1);
        response.put("categoryName", "Java");

        ResponseEntity<?> respEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);
        doReturn(respEntity).when(categoryService).saveCategory(any(Category.class));

        mockMvc.perform(post("/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Category created successfully"))
                .andExpect(jsonPath("$.categoryId").value(1));

        verify(categoryService, times(1)).saveCategory(any(Category.class));
    }

    @Test
    @DisplayName("Should update category description successfully")
    void createCategory_updateExisting() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Category description updated successfully");
        response.put("categoryId", 1);
        response.put("categoryName", "Java");
        response.put("updatedRows", 1);

        ResponseEntity<?> respEntity = ResponseEntity.ok(response);
        doReturn(respEntity).when(categoryService).saveCategory(any(Category.class));

        mockMvc.perform(post("/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Category description updated successfully"));

        verify(categoryService, times(1)).saveCategory(any(Category.class));
    }

    @Test
    @DisplayName("Should return all categories")
    void getCategories_success() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "No of categories fetched: 2");
        response.put("data", categoryList);

        ResponseEntity<?> respEntity = ResponseEntity.ok(response);
        doReturn(respEntity).when(categoryService).getCategories();

        mockMvc.perform(get("/category/allCategories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].categoryName").value("Java"))
                .andExpect(jsonPath("$.data[1].categoryName").value("Python"));

        verify(categoryService, times(1)).getCategories();
    }

    @Test
    @DisplayName("Should return empty list when no categories exist")
    void getCategories_empty() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "No of categories fetched: 0");
        response.put("data", Arrays.asList());

        ResponseEntity<?> respEntity = ResponseEntity.ok(response);
        doReturn(respEntity).when(categoryService).getCategories();

        mockMvc.perform(get("/category/allCategories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));

        verify(categoryService, times(1)).getCategories();
    }

    @Test
    @DisplayName("Should fail when category name is empty")
    void createCategory_emptyName() throws Exception {
        Category invalidCategory = new Category();
        invalidCategory.setCategoryName("");
        invalidCategory.setDescription("Description");

        mockMvc.perform(post("/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategory)))
                .andExpect(status().isBadRequest());

        verify(categoryService, times(0)).saveCategory(any(Category.class));
    }

    @Test
    @DisplayName("Should accept empty description when only @NotNull is present")
    void createCategory_emptyDescription() throws Exception {
        Category invalidCategory = new Category();
        invalidCategory.setCategoryName("Java");
        invalidCategory.setDescription(""); // empty but not null

        // Stub the service because validation passes (description is not null)
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Category created successfully");
        response.put("categoryId", 1);

        ResponseEntity<?> respEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);
        doReturn(respEntity).when(categoryService).saveCategory(any(Category.class));

        mockMvc.perform(post("/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Category created successfully"));

        verify(categoryService, times(1)).saveCategory(any(Category.class));
    }

    @Test
    @DisplayName("Should fail when category name is null")
    void createCategory_nullName() throws Exception {
        Category invalidCategory = new Category();
        invalidCategory.setCategoryName(null);
        invalidCategory.setDescription("Description");

        mockMvc.perform(post("/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategory)))
                .andExpect(status().isBadRequest());

        verify(categoryService, times(0)).saveCategory(any(Category.class));
    }

    @Test
    @DisplayName("Should fail when category description is null")
    void createCategory_nullDescription() throws Exception {
        Category invalidCategory = new Category();
        invalidCategory.setCategoryName("Java");
        invalidCategory.setDescription(null);

        mockMvc.perform(post("/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategory)))
                .andExpect(status().isBadRequest());

        verify(categoryService, times(0)).saveCategory(any(Category.class));
    }

    @Test
    @DisplayName("Should handle special characters in category name")
    void createCategory_specialCharacters() throws Exception {
        Category specialCategory = new Category();
        specialCategory.setCategoryName("C++ & C#");
        specialCategory.setDescription("Programming Languages");

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Category created successfully");
        response.put("categoryId", 3);

        ResponseEntity<?> respEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);
        doReturn(respEntity).when(categoryService).saveCategory(any(Category.class));

        mockMvc.perform(post("/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(specialCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));

        verify(categoryService, times(1)).saveCategory(any(Category.class));
    }

    @Test
    @DisplayName("Should handle long category name")
    void createCategory_longName() throws Exception {
        Category longNameCategory = new Category();
        longNameCategory.setCategoryName("This is a very long category name for testing purposes to ensure the system can handle lengthy input");
        longNameCategory.setDescription("Long name description");

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Category created successfully");

        ResponseEntity<?> respEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);
        doReturn(respEntity).when(categoryService).saveCategory(any(Category.class));

        mockMvc.perform(post("/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(longNameCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));

        verify(categoryService, times(1)).saveCategory(any(Category.class));
    }

    @Test
    @DisplayName("Should verify correct endpoint paths")
    void verifyEndpointPaths() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);

        ResponseEntity<?> respEntity = ResponseEntity.ok(response);
        doReturn(respEntity).when(categoryService).getCategories();

        // Test GET /category/all endpoint
        mockMvc.perform(get("/category/allCategories"))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).getCategories();
    }
}