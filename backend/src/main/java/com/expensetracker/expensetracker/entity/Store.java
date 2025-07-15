package com.expensetracker.expensetracker.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location")
    private String location;

    public void setName(String name) {
        this.name = name;
    }

    // Getters and Setters
}
