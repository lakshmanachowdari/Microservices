package com.lakshman.question_service.Repository;

import com.lakshman.question_service.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionJpaRepository extends JpaRepository<Question, Integer> {
    List<Question> findByCategory(String category);

    @Query(value = "select id from questions where category = :category " +
            "and is_deleted = false order by random() limit :numQ", nativeQuery = true)
    List<Integer> getRandomQuestionIdsByCategory(@Param("category") String category,
                                                 @Param("numQ") Integer numQ);
}
