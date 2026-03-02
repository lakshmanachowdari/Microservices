package com.lakshman.quiz_service.Constants;

public  final class MarketDataConstants {

    public static final String CREATE_QUIZ = "create";
    public static final String GET_QUIZ = "get/{id}";
    public static final String SUBMIT_RESULT = "submit/{id}";
    public static final String GET_ALL_QUIZ = "allQuiz's";
    public static final String TEST_RESPONSE = "response/{id}";

    public static final String GENERATE_QUESTIONS = "question/generate";
    public static final String GET_RESPONSE = "question/score";
    public static final String GET_QUESTION_IDS = "question/getQuestions";
    public static final String GET_TEST_RESPONSE = "question/check";
}
