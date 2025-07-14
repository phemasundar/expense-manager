package com.expensetracker.expensetracker.service;

import com.expensetracker.expensetracker.entity.User;

public interface UserService {
    User findOrCreateUser(String clerkUserId, String email);
}
