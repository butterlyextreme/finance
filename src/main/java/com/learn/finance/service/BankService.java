package com.learn.finance.service;

import com.learn.finance.model.producer.Bank;
import reactor.core.publisher.Mono;

public interface BankService {

    Mono<Bank> getBank(final String swiftCode);
}
