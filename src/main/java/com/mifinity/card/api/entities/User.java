package com.mifinity.card.api.entities;

import com.mifinity.card.api.enums.ProfileEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Document
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Email required")
    @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "Password required")
    @Size(min = 6, max = 8, message = "Password must be length from 6 to 8")
    private String password;

    private ProfileEnum profile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ProfileEnum getProfile() {
        return profile;
    }

    public void setProfile(ProfileEnum profile) {
        this.profile = profile;
    }
}
