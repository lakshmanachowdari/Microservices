package com.lakshman.quiz_service.Repository;


import com.lakshman.quiz_service.Entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizJpaRepository extends JpaRepository<Quiz, Integer> {
}
