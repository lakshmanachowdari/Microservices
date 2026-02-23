package com.lakshman.question_service.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name  = "question", nullable = false)
    private String question;

    @Column(name  = "option1", nullable = false)
    private String option1;

    @Column(name  = "option2", nullable = false)
    private String option2;

    @Column(name  = "option3", nullable = false)
    private String option3;

    @Column(name  = "option4", nullable = false)
    private String option4;

    @Column(name  = "answer", nullable = false)
    private String answer;

    @NotNull(message = "Category Id can't be null")
    @JoinColumn(name = "category_id", nullable = false)
    private Integer categoryId;

    @NotBlank(message = "Question cannot be empty")
    @Column(name  = "question_level", nullable = false)
    private String questionLevel;

    @Column(name  = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted = false;
}
