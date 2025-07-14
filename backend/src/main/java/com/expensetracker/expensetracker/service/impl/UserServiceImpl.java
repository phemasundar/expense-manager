package com.expensetracker.expensetracker.service.impl;

import com.expensetracker.expensetracker.entity.User;
import com.expensetracker.expensetracker.repository.UserRepository;
import com.expensetracker.expensetracker.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findOrCreateUser(String clerkUserId, String email) {
        return userRepository.findByClerkId(clerkUserId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setClerkId(clerkUserId);
                    newUser.setEmail(email);
                    return userRepository.save(newUser);
                });
    }
}
