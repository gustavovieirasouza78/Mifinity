package com.mifinity.card.api.services.impl;

import com.mifinity.card.api.entities.User;
import com.mifinity.card.api.repositories.UserRepository;
import com.mifinity.card.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public User insert(User user) {
        return this.userRepository.insert(user);
    }

    @Override
    public Optional<User> findById(String id) {
        return this.userRepository.findById(id);
    }

}
