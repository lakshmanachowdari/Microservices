package com.lakshman.question_service.Repository;

import com.lakshman.question_service.Entity.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driver-class-name=org.h2.Driver"
})
@DisplayName("CategoryJpaRepository tests")
class CategoryJpaRepositoryTest {

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    private Category catJava;

    @BeforeEach
    void setUp() {
        catJava = new Category();
        catJava.setCategoryName("Java");
        catJava.setDescription("Java language");
    }

    @Test
    @DisplayName("findByCategoryName returns null when category does not exist")
    void findByCategoryName_whenNotExists_returnsNull() {
        Integer id = categoryJpaRepository.findByCategoryName("NonExisting");
        assertThat(id).isNull();
    }

    @Test
    @DisplayName("updateDescriptionByCategoryName returns 0 when id not present")
    void updateDescriptionByCategoryName_nonExistingId_returnsZero() {
        int rows = categoryJpaRepository.updateDescriptionByCategoryName(99999, "No effect");
        assertThat(rows).isEqualTo(0);
    }

    @Test
    @DisplayName("findByCategoryName rejects blank input if validation is active")
    void findByCategoryName_blankInput_behavior() {
        try {
            Integer id = categoryJpaRepository.findByCategoryName("");
            assertThat(id).isNull();
        } catch (Exception ex) {
            assertThat(ex).isInstanceOfAny(IllegalArgumentException.class, InvalidDataAccessApiUsageException.class);
        }
    }
}