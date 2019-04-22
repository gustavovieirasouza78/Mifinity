package com.mifinity.card.api.controllers;


import com.mifinity.card.api.entities.Card;
import com.mifinity.card.api.entities.User;
import com.mifinity.card.api.response.Response;
import com.mifinity.card.api.security.jwt.JwtTokenUtil;
import com.mifinity.card.api.services.CardService;
import com.mifinity.card.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/api/card")
@CrossOrigin(origins = "*")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    protected JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping()
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public ResponseEntity<Response<Card>> create(HttpServletRequest request, @RequestBody Card card,
                                                 BindingResult result) {
        Response<Card> response = new Response<>();
        try {
            validateCreateCard(card, result);
            if (result.hasErrors()) {
                result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
                return ResponseEntity.badRequest().body(response);
            }
            card.setUserCreator(userFromRequest(request));
            Card cardPersisted = (Card) cardService.createOrUpdate(card);
            response.setData(cardPersisted);
        } catch (Exception e) {
            response.getErrors().add(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    private void validateCreateCard(Card card, BindingResult result) {
        if (card.getExpiration() == null) {
            result.addError(new ObjectError("Data", "Expiration Data need be informed"));
        }
        Optional<Card> cardCurrentOptional = cardService.findByNumberSpecific(card.getNumber());
        if(cardCurrentOptional.isPresent()){
            result.addError(new ObjectError("Number", "This card already exist"));
        }
        return;
    }

    public User userFromRequest(HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        String email = jwtTokenUtil.getUserNameFromToken(token);
        return userService.findByEmail(email);
    }

    @PutMapping()
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public ResponseEntity<Response<Card>> update(HttpServletRequest request, @RequestBody Card card,
                                                 BindingResult result) {
        Response<Card> response = new Response<Card>();
        try {
            validateUpdateCard(card, result);
            if (result.hasErrors()) {
                result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
                return ResponseEntity.badRequest().body(response);
            }
            Optional<Card> cardCurrentOptional = cardService.findByNumberSpecific(card.getNumber());
            Card cardCurrent = cardCurrentOptional.get();
            cardCurrent.setExpiration(card.getExpiration());

            Card cardPersisted = (Card) cardService.createOrUpdate(cardCurrent);

            response.setData(cardPersisted);
        } catch (Exception e) {
            response.getErrors().add(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    private void validateUpdateCard(Card card, BindingResult result) {
        if (card.getNumber() == null) {
            result.addError(new ObjectError("Card", "Card need to be informed!"));
            return;
        }
        if (card.getExpiration() == null) {
            result.addError(new ObjectError("Data", "Expiration Data need be informed"));
            return;
        }
    }

    @GetMapping(value = "{page}/{count}/{number}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<Response<Page<Card>>> findCards(HttpServletRequest request,
                                                             @PathVariable int page,
                                                             @PathVariable int count,
                                                             @PathVariable String number) throws Exception {


        Response<Page<Card>> response = new Response<Page<Card>>();
        Page<Card> cards = null;

        User userRequest = userFromRequest(request);
        if(userRequest.getProfile().name().equals("ROLE_ADMIN")) {
            cards = cardService.findByNumber(page, count, number);
        }else{
            cards = cardService.findByNumberAndCreator(page, count, number, userRequest.getId());
        }

        response.setData(cards);
        return ResponseEntity.ok(response);
    }
}
