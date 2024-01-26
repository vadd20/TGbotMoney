package com.example.TGbotMoney.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "data")
public class User {
    @Id
    private long id;
    private String name;
}
