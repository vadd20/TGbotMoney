package com.example.TGbotMoney.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    private static final DateTimeFormatter todayFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm");
    public String toStringForToday() {
        return "Income: " + category + " - " + sum + " - " + date.format(todayFormatter);
    }
    @Override
    public String toString() {
        return "Income: " + category + " - " + sum + " - " + date.format(formatter);
    }
}
