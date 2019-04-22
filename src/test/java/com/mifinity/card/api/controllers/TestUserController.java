package com.mifinity.card.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mifinity.card.api.entities.User;
import com.mifinity.card.api.enums.ProfileEnum;
import com.mifinity.card.api.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TestUserController {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;


    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    public void testFindById() throws Exception {
        BDDMockito
                .given(this.userService.findById(Mockito.anyString()))
                .willReturn(Optional.of(this.obterUser()));

        mvc.perform(MockMvcRequestBuilders.get("/api/user/123")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("test@test.com"))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    public void testInsert() throws Exception{
        User user = obterUser();
        BDDMockito
                .given(this.userService.insert(Mockito.any()))
                .willReturn(user);

        mvc.perform(MockMvcRequestBuilders.post("/api/user/create")
                .content(this.obterJson(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("test@test.com"))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    public User obterUser(){
        User user = new User();
        user.setEmail("test@test.com");
        user.setProfile(ProfileEnum.ROLE_CUSTOMER);
        user.setPassword("123");
        return user;
    }

    public String obterJson(User user) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(user);
    }
}
