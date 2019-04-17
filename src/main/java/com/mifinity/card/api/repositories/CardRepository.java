package com.mifinity.card.api.repositories;

import com.mifinity.card.api.entities.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface CardRepository extends MongoRepository<Card, String> {

    Optional<Card> findByNumber(String number);

    Page<Card> findByNumberContainingOrderByNumberDesc(Pageable pages, String number);

    Page<Card> findByNumberContainingAndUserCreatorIdOrderByNumberDesc(Pageable pages, String number, String userCreator);
}
