package com.learn.finance.controller;

import com.learn.finance.model.producer.AddComment;
import com.learn.finance.model.producer.Bank;
import com.learn.finance.service.BankService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BankController {

    private final BankService bankService;
    @RequestMapping(value = "/swift/{code}", method = RequestMethod.GET, produces = { "application/json" })
    public Mono<Bank> getGameById(@PathVariable final String code) {
        return bankService.getBank(code);
    }

    @PostMapping(value = "/swift/{id}/comment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Bank> createComment(
            @PathVariable final String id, @Valid @RequestBody AddComment comment) {
        return bankService.addComment(id, comment.getText());
    }
}
