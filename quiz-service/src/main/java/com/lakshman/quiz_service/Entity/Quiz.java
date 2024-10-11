package com.lakshman.quiz_service.Entity;

import com.lakshman.quiz_service.Wrapper.QuestionWrapper;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Quiz {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;
    private String title;
    @ElementCollection
    private List<Integer> questionIds;
}
