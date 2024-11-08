package com.learn.finance.service;

import com.learn.finance.model.consumer.BankDetails;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SWIFTClient {

    Mono<List<BankDetails>> getBankDetails(final String swiftCode);

}
