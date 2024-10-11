package com.lakshman.question_service.Repository;

import com.lakshman.question_service.Entity.Question;
import com.lakshman.question_service.Entity.SubmitResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByCategory(String category);

    @Query(value = "select * from questions where category = :category " +
            "and is_deleted = false order by random() limit :numQ", nativeQuery = true)
    List<Question> getRandomQuestionByCategory(int numQ, String category);

    @Query(value = "select id from questions where category = :category " +
            "and is_deleted = false order by random() limit :numQ", nativeQuery = true)
    List<Integer> getRandomQuestionIdsByCategory(String category, Integer numQ);

    @Query(value = "select id,answer from questions where id in (:ids)", nativeQuery = true)
    List<SubmitResult> getCorrectAnsByIds(List<Integer> ids);
}
