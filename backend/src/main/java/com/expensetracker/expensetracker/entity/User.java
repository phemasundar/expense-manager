package com.expensetracker.expensetracker.entity;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "clerk_id", unique = true, nullable = false)
    private String clerkId;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    // Getters and Setters
}
