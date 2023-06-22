package com.example.TGbotMoney.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "income")
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long income_id;
    private long user_id;
    private LocalDateTime date;
    private String category;
    private int sum;
}
