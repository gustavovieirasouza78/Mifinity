package com.mifinity.card.api.services;

import com.mifinity.card.api.entities.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CardService {

    Card createOrUpdate(Card card);

    Optional<Card> findByNumberSpecific(String number);

    Page<Card> findAll(int page, int count);

    Page<Card> findAllByUserCreator(int page, int count, String userId);

    Page<Card> findByNumber(int page, int count, String number);

    Page<Card> findByNumberAndCreator(int page, int count, String number, String userId);

}