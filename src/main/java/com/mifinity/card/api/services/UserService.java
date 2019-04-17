package com.mifinity.card.api.services;

import com.mifinity.card.api.entities.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {
    User findByEmail(String email);

    User insert(User user);

    Optional<User> findById(String id);
}
