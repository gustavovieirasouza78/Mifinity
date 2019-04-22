package com.mifinity.card.api.controllers;

import com.mifinity.card.api.entities.User;
import com.mifinity.card.api.response.Response;
import com.mifinity.card.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping()
    @RequestMapping("/create")
//  @PreAuthorize("hasAnyRole('ADMIN')") Maybe only ADMINs could create users
    public ResponseEntity<Response<User>> insert(HttpServletRequest request, @RequestBody User user,
                                                 BindingResult result) {
        Response<User> response = new Response<User>();
        try {
            validateCreateUser(user, result);
            if (result.hasErrors()) {
                result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
                return ResponseEntity.badRequest().body(response);
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User userPersisted = (User) userService.insert(user);
            userPersisted.setPassword(null);
            response.setData(userPersisted);
        } catch (DuplicateKeyException dE) {
            response.getErrors().add("E-mail already registered !");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.getErrors().add(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    private void validateCreateUser(User user, BindingResult result) {
        if (user.getEmail() == null) {
            result.addError(new ObjectError("User", "Email not informed"));
            return;
        }
    }

    @GetMapping(value = "{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<User>> findById(@PathVariable("id") String id) {
        Response<User> response = new Response<User>();
        Optional<User> userOptional = userService.findById(id);
        User user = userOptional.get();
        if (user == null) {
            response.getErrors().add("Register not found id:" + id);
            return ResponseEntity.badRequest().body(response);
        }
        user.setPassword(null);
        response.setData(user);
        return ResponseEntity.ok(response);
    }
}