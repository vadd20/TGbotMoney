package com.example.TGbotMoney.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@Table(name = "expense")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long expense_id;
    private long user_id;
    private LocalDateTime date;
    private String category;
    private int sum;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm");
    @Override
    public String toString() {
        return "Expense: " + category + " - " + sum + " - " + date.format(formatter);
    }
}

