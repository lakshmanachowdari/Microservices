package com.lakshman.question_service.MockData;

import com.lakshman.question_service.Entity.Question;

import java.util.List;

public class DataUtils {

    public Question getQuestion() {
        Question question = new Question();
        question.setId(1);
        question.setQuestion("What is Java??");
        question.setOption1("1");
        question.setOption2("2");
        question.setOption3("2");
        question.setOption4("2");
        question.setAnswer("2");
        question.setQuestionLevel("Easy");
        question.setCategory("Java");

        return question;
    }

    public List<Question> getQuestionList() {
        Question q1 = new Question();
        q1.setId(1);
        q1.setQuestion("What is Java?");
        q1.setCategory("JAVA");

        Question q2 = new Question();
        q2.setId(2);
        q2.setQuestion("What is Spring Boot?");
        q2.setCategory("SPRING");

        return List.of(q1,q2);
    }
}
