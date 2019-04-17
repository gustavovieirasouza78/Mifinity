package com.mifinity.card.api.services.impl;

import com.mifinity.card.api.entities.Card;
import com.mifinity.card.api.repositories.CardRepository;
import com.mifinity.card.api.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    CardRepository cardRepository;

    @Override
    public Optional<Card> findByNumberSpecific(String number) {
        return this.cardRepository.findByNumber(number);
    }

    @Override
    public Card createOrUpdate(Card ticket) {
        return this.cardRepository.save(ticket);
    }

    @Override
    public Page<Card> findByNumber(int page, int count, String number) {
        return this.cardRepository.findByNumberContainingOrderByNumberDesc(PageRequest.of(page, count), number);
    }

    @Override
    public Page<Card> findByNumberAndCreator(int page, int count, String number, String userId) {
        return this.cardRepository.findByNumberContainingAndUserCreatorIdOrderByNumberDesc(PageRequest.of(page,count),number,userId);
    }
}
