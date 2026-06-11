package com.lakshman.question_service.Repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryJdbcRepository Unit Tests")
class CategoryJdbcRepositoryTest {

    @Mock
    private NamedParameterJdbcTemplate jdbc;

    @InjectMocks
    private CategoryJdbcRepository repository;

    @Captor
    private ArgumentCaptor<MapSqlParameterSource> paramCaptor;

    @Captor
    private ArgumentCaptor<String> sqlCaptor;

    @BeforeEach
    void setUp() {
        // repository is injected with mocked jdbc by Mockito
    }

    @Test
    @DisplayName("findAllByCategoryName - returns mapping for given categories and passes correct SQL and params")
    void findAllByCategoryName_returnsMapping_and_passesSqlAndParams() {
        Set<String> categories = new HashSet<>(Arrays.asList("CatA", "CatB"));

        Map<String, Integer> expected = new HashMap<>();
        expected.put("CatA", 10);
        expected.put("CatB", 20);

        // Stub jdbc.query to return expected map
        when(jdbc.query(anyString(), any(MapSqlParameterSource.class), any(ResultSetExtractor.class)))
                .thenReturn(expected);

        Map<String, Integer> result = repository.findAllByCategoryName(categories);

        // verify return
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(10, result.get("CatA"));
        assertEquals(20, result.get("CatB"));

        // capture args used when calling jdbc.query
        verify(jdbc, times(1)).query(sqlCaptor.capture(), paramCaptor.capture(), any(ResultSetExtractor.class));

        String usedSql = sqlCaptor.getValue();
        assertNotNull(usedSql);
        // Basic checks on SQL text (case-insensitive)
        String lower = usedSql.toLowerCase(Locale.ROOT);
        assertTrue(lower.contains("select"));
        assertTrue(lower.contains("id"));
        assertTrue(lower.contains("category"));
        assertTrue(lower.contains("where"));
        assertTrue(lower.contains("in ("));

        MapSqlParameterSource usedParams = paramCaptor.getValue();
        assertNotNull(usedParams);

        Map<String, Object> values = usedParams.getValues();
        assertTrue(values.containsKey("categoryName"));

        Object paramValue = values.get("categoryName");
        assertTrue(paramValue instanceof Collection, "categoryName param should be a Collection");
        @SuppressWarnings("unchecked")
        Collection<String> passedCategories = (Collection<String>) paramValue;
        assertEquals(2, passedCategories.size());
        assertTrue(passedCategories.containsAll(categories));
    }

    @Test
    @DisplayName("findAllByCategoryName - handles empty result")
    void findAllByCategoryName_emptyResult() {
        Set<String> categories = new HashSet<>(Collections.singletonList("NoSuch"));

        // return empty map
        when(jdbc.query(anyString(), any(MapSqlParameterSource.class), any(ResultSetExtractor.class)))
                .thenReturn(Collections.emptyMap());

        Map<String, Integer> result = repository.findAllByCategoryName(categories);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(jdbc, times(1)).query(sqlCaptor.capture(), paramCaptor.capture(), any(ResultSetExtractor.class));
        MapSqlParameterSource usedParams = paramCaptor.getValue();
        Map<String, Object> values = usedParams.getValues();
        assertTrue(values.containsKey("categoryName"));
    }
}