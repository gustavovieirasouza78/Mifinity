package com.mifinity.card.api.services;

import com.mifinity.card.api.entities.User;
import com.mifinity.card.api.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Before
    public void setUp() throws Exception{
        BDDMockito
                .given(this.userRepository.findByEmail(Mockito.anyString()))
                .willReturn(new User());
        BDDMockito
                .given(this.userRepository.findById(Mockito.anyString()))
                .willReturn(Optional.of(new User()));
        BDDMockito
                .given(this.userRepository.insert((User) Mockito.any()))
                .willReturn(new User());
    }

    @Test
    public void testFindByEmail() {
        User user = this.userService.findByEmail("test@test.com");
        Assert.assertNotNull(user);
    }

    @Test
    public void testInsert() {
        User user = this.userService.insert(new User());
        Assert.assertNotNull(user);
    }

    @Test
    public void testFindById() {
        Optional<User> user = this.userService.findById("1");
        Assert.assertNotNull(user.isPresent());
    }
}