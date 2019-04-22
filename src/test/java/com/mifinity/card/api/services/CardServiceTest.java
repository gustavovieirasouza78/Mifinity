package com.mifinity.card.api.services;

import com.mifinity.card.api.entities.Card;
import com.mifinity.card.api.repositories.CardRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CardServiceTest {

    @Autowired
    private CardService cardService;

    @MockBean
    private CardRepository cardRepository;


    @Before
    public void setUp() throws Exception{
        Card card = new Card();
        card.setNumber("1");
        List<Card> cards = new ArrayList<>();
        cards.add(card);
        Page<Card> pagedResponse = new PageImpl<>(cards);
        BDDMockito
                .given(this.cardRepository.findByNumber(Mockito.anyString()))
                .willReturn(Optional.of(card));
        BDDMockito
                .given(this.cardRepository.findByNumberContainingOrderByNumberDesc(Mockito.any(),Mockito.anyString()))
                .willReturn(pagedResponse);
        BDDMockito.given(this.cardRepository
                .findByNumberContainingAndUserCreatorIdOrderByNumberDesc(Mockito.any(),Mockito.anyString(),Mockito.anyString()))
                .willReturn(pagedResponse);
        BDDMockito
                .given(this.cardRepository.save(Mockito.any()))
                .willReturn(card);
    }

    @Test
    public void testCreateOrUpdate(){
        Card card = this.cardService.createOrUpdate(new Card());
        Assert.assertNotNull(card);
    }

    @Test
    public void testFindByNumberSpecific(){
        Optional<Card> card =  this.cardService.findByNumberSpecific("1");
        Assert.assertTrue(card.isPresent());
    }

    @Test
    public void testFindByNumber(){
        Page<Card> cards = this.cardService.findByNumber(0, 1, "1");
        Assert.assertEquals(1l, cards.getTotalElements());
    }

    @Test
    public void testFindByNumberAndCreator(){
        Page<Card> cards = this.cardService.findByNumberAndCreator(0, 1, "1", "");
        Assert.assertEquals(1l, cards.getTotalElements());
    }


}
