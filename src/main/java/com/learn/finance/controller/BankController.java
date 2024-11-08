package com.learn.finance.controller;

import com.learn.finance.model.producer.Bank;
import com.learn.finance.service.BankService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
}
