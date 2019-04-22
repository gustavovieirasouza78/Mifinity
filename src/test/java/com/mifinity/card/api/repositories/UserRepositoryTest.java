package com.mifinity.card.api.repositories;

import com.mifinity.card.api.entities.User;
import com.mifinity.card.api.enums.ProfileEnum;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        User user = new User();
        user.setEmail("test@test.com");
        user.setProfile(ProfileEnum.ROLE_ADMIN);
        this.userRepository.save(user);
    }

    @After
    public void tearDown() throws Exception {
        this.userRepository.deleteAll();
    }

    @Test
    public void testFindByEmail() {
        User user = this.userRepository.findByEmail("test@test.com");
        Assert.assertNotNull(user);
    }
}
