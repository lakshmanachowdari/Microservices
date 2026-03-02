package com.lakshman.quiz_service.Wrapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TestResponse {
    private int id;
    private String response;
}
