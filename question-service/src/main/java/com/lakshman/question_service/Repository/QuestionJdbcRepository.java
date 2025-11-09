package com.lakshman.question_service.Repository;

import com.lakshman.question_service.Entity.SubmitResult;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestionJdbcRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public QuestionJdbcRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<SubmitResult> getCorrectAnsByIds(@Param("ids") List<Integer> ids){
        var sql = "select id,answer from questions where id in (:ids)";
        var params = new MapSqlParameterSource()
                .addValue("ids", ids);

        /* we can use below mapper in the jdbc query template */

        /*
        RowMapper<SubmitResult> rm = (rs, rowNum) -> {
            SubmitResult submitResult = new SubmitResult();
            submitResult.setId(rs.getInt("id"));
            submitResult.setResponse(rs.getString("answer"));
            return submitResult;
        };
        */
        
        return jdbc.query(sql, params, (rs, n) ->
                new SubmitResult(rs.getInt("id"), rs.getString("answer")));
    }

}
