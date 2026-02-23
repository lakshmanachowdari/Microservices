package com.lakshman.question_service.Repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Repository
public class CategoryJdbcRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public CategoryJdbcRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Map<String, Integer> findAllByCategoryName(Set<String> categoryName){
        var sql = "select id, category from categories where category in (:categoryName)";
        var params = new MapSqlParameterSource()
                .addValue("categoryName", categoryName);

        return jdbc.query(sql, params, rs -> {
            Map<String, Integer> result = new HashMap<>();
            while (rs.next()){
                result.put(rs.getString("category"), rs.getInt("id"));
            }
            return result;
        });
    }
}
