package com.learn.finance.service;

import com.learn.finance.data.entity.BankEntity;
import com.learn.finance.data.repository.BankRepository;
import com.learn.finance.model.consumer.BankDetails;
import com.learn.finance.model.producer.Bank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class BankServiceImpl implements BankService {

    private BankRepository bankRepository;
    private SWIFTClient swiftClient;

    @Override
    public Mono<Bank> getBank(String swiftCode) {
        return getCachedBank(swiftCode).switchIfEmpty(Mono.defer(() -> retrieveBank(swiftCode)));
    }

    private Mono<Bank> retrieveBank(String swiftCode) {
        return swiftClient.getBankDetails(swiftCode)
                .map(this::toBankFromDetails)
                .map(this::cacheBank)
                .doOnError(err -> log.error(err.toString()))
                .doFinally(__ -> log.info("Retrieved bank from service for swiftcode [{}]", swiftCode));
    }

    private Bank cacheBank(Bank bank) {
        BankEntity bankEntity = BankEntity.builder()
                .bic(bank.getSwiftCode())
                .bankName(bank.getBankOrInstitution())
                .city(bank.getCity()).build();
        bankRepository.save(bankEntity);
        log.info("Saved bank details in cache for bank code [{}]",  bank.getSwiftCode());
        return bank;
    }

    private Mono<Bank> getCachedBank(String swiftCode) {
        return Optional.ofNullable(bankRepository.findByBic(swiftCode))
                .map(this::toBankFromEntity).orElse(Mono.empty());
    }

    private Mono<Bank> toBankFromEntity(BankEntity entity) {
        return Mono.just(Bank.builder()
                .bankOrInstitution(entity.getBankName())
                .swiftCode(entity.getBic())
                .city(entity.getCity())
                .cached(true).build());
    }

    private Bank toBankFromDetails(List<BankDetails> bankDetailsList) {
        if (!bankDetailsList.isEmpty()) {
            BankDetails bankDetails = bankDetailsList.get(0);
            return Bank.builder()
                    .bankOrInstitution(bankDetails.getBankOrInstitution())
                    .city(bankDetails.getCity())
                    .swiftCode(bankDetails.getSwiftCode()).build();
        } else return null;
    }
}
