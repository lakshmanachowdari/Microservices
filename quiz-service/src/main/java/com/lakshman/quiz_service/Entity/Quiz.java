package com.lakshman.quiz_service.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ElementCollection;
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
