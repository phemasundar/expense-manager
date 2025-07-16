package com.expensetracker.expensetracker.service.impl;

import com.expensetracker.expensetracker.entity.User;
import com.expensetracker.expensetracker.repository.UserRepository;
import com.expensetracker.expensetracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
