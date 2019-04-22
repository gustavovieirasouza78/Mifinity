package com.mifinity.card.api.repositories;

import com.mifinity.card.api.entities.Card;
import com.mifinity.card.api.entities.User;
import com.mifinity.card.api.enums.ProfileEnum;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception{
        User user = new User();
        user.setEmail("test@test.com");
        user.setProfile(ProfileEnum.ROLE_ADMIN);


        Card card = new Card();
        card.setName("Test Card");
        card.setNumber("123456789012");
        card.setExpiration(new Date());
        card.setUserCreator(this.userRepository.save(user));

        this.cardRepository.save(card);
    }

    @After
    public void tearDown() throws Exception{
        this.cardRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    public void testFindByNumber(){
        Optional<Card> card = this.cardRepository.findByNumber("123456789012");
        Assert.assertTrue(card.isPresent());
    }

    @Test
    public void testFindByNumberContainingOrderByNumberDesc(){
        Pageable page = PageRequest.of(0,1);
        Page<Card> cards = this.cardRepository.findByNumberContainingOrderByNumberDesc(page, "5678");

        Assert.assertTrue(cards.getSize()>0);
    }

    @Test
    public void testfindByNumberContainingAndUserCreatorIdOrderByNumberDesc(){
        Pageable page = PageRequest.of(0,1);
        User user = this.userRepository.findByEmail("test@test.com");

        Page<Card> cards = this.cardRepository.findByNumberContainingAndUserCreatorIdOrderByNumberDesc(page, "5678", user.getId());

        Assert.assertTrue(cards.getSize()>0);
    }
}
