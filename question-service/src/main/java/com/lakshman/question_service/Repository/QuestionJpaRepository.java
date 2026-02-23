package com.lakshman.question_service.Repository;

import com.lakshman.question_service.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionJpaRepository extends JpaRepository<Question, Integer> {
    List<Question> findByCategoryId(Integer categoryId);

    @Query(value = "select id from questions where category_id = :categoryId " +
            "and is_deleted = false order by random() limit :numQ", nativeQuery = true)
    List<Integer> getRandomQuestionIdsByCategory(@Param("categoryId") Integer categoryId,
                                                 @Param("numQ") Integer numQ);

    @Modifying
    @Query(value =  "update questions q set is_deleted  = true where q.id in (" +
            "select q.id from questions q where ctid not in (select min(ctid) from questions group by question)" +
            " and q.is_deleted = false)", nativeQuery = true)
    Integer deleteDuplicates();
}
