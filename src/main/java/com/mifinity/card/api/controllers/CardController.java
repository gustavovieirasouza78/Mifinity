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
            validateCreateTicket(card, result);
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

    private void validateCreateTicket(Card card, BindingResult result) {
        if (card.getExpiration() == null) {
            result.addError(new ObjectError("Data", "Expiration Data need be informed"));
            return;
        }
    }

    public User userFromRequest(HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        String email = jwtTokenUtil.getUserNameFromToken(token);
        return userService.findByEmail(email);
    }

    @PutMapping()
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<Response<Card>> update(HttpServletRequest request, @RequestBody Card card,
                                                 BindingResult result) {
        Response<Card> response = new Response<Card>();
        try {
            validateUpdateTicket(card, result);
            if (result.hasErrors()) {
                result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
                return ResponseEntity.badRequest().body(response);
            }
            Optional<Card> cardCurrentOptional = cardService.findByNumberSpecific(card.getNumber());
            Card cardCurrent = cardCurrentOptional.get();
            card.setNumber(cardCurrent.getNumber());
            card.setName(cardCurrent.getName());
            card.setId(cardCurrent.getId());
            if(cardCurrent.getUserCreator() != null) {
                card.setUserCreator(cardCurrent.getUserCreator());
            }
            Card cardPersisted = (Card) cardService.createOrUpdate(card);
            response.setData(cardPersisted);
        } catch (Exception e) {
            response.getErrors().add(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    private void validateUpdateTicket(Card card, BindingResult result) {
        if (card.getNumber() == null) {
            result.addError(new ObjectError("Card", "Card need to be informed!"));
            return;
        }
    }

    @GetMapping(value = "{page}/{count}/{number}/{creator}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<Response<Page<Card>>> findForCustomer(HttpServletRequest request,
                                                             @PathVariable int page,
                                                             @PathVariable int count,
                                                             @PathVariable String number,
                                                             @PathVariable String creator) throws Exception {


        Response<Page<Card>> response = new Response<Page<Card>>();
        Page<Card> cards = null;

        User userRequest = userFromRequest(request);

        cards = cardService.findByNumberAndCreator(page, count, number, userRequest.getId());

        response.setData(cards);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "{page}/{count}/{number}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<Page<Card>>> findForAdmin(HttpServletRequest request,
                                                                @PathVariable int page,
                                                                @PathVariable int count,
                                                                @PathVariable String number) throws Exception {


        Response<Page<Card>> response = new Response<Page<Card>>();
        Page<Card> cards = null;

        cards = cardService.findByNumber(page, count, number);

        response.setData(cards);
        return ResponseEntity.ok(response);
    }
}
