package com.learn.finance.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.finance.config.DownStreamConfiguration;
import com.learn.finance.config.DownStreamProperties;
import com.learn.finance.data.entity.BankEntity;
import com.learn.finance.data.repository.BankRepository;
import com.learn.finance.exception.DownStreamException;
import com.learn.finance.model.consumer.BankDetails;
import com.learn.finance.model.producer.Bank;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.learn.finance.utils.TestUtils.deserialize;
import static com.learn.finance.utils.TestUtils.readMockJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {BankServiceImpl.class, SWIFTClientImpl.class, BankRepository.class, DownStreamConfiguration.class, JacksonAutoConfiguration.class,
                WebClientAutoConfiguration.class})
@EnableConfigurationProperties({DownStreamProperties.class})
@TestPropertySource("classpath:application-test.properties")
class BankServiceImplTest {

    private static final String SWIFTCODE = "CRBAALTRXXX";

    @MockBean
    SWIFTClient swiftClient;
    @MockBean
    BankRepository bankRepository;

    @Autowired
    BankService bankService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void test_find_bank_from_downstream() {
        List<BankDetails> bankDetailsList = deserialize(objectMapper, readMockJson("bank_details.json"), new TypeReference<List<BankDetails>>() {
        });
        when(bankRepository.findByBic(eq(SWIFTCODE))).thenReturn(null);
        when(swiftClient.getBankDetails(eq(SWIFTCODE))).thenReturn(Mono.just(bankDetailsList));

        Bank bank = Bank.builder().bankOrInstitution("ALPHA BANK ALBANIA")
                .city("TIRANA")
                .swiftCode("CRBAALTRXXX")
                .cached(false)
                .build();

        StepVerifier.create(bankService.getBank(SWIFTCODE)).
                assertNext(response ->
                        assertEquals(bank,
                                response))
                .verifyComplete();

        verify(bankRepository, times(1)).findByBic(eq(SWIFTCODE));
        verify(swiftClient, times(1)).getBankDetails(eq(SWIFTCODE));


    }

    @Test
    void test_find_bank_from_cache() {
        Bank bank = Bank.builder().bankOrInstitution("ALPHA BANK ALBANIA")
                .city("TIRANA")
                .swiftCode("CRBAALTRXXX")
                .cached(true)
                .build();
        BankEntity entity = BankEntity.builder().bankName("ALPHA BANK ALBANIA")
                .city("TIRANA")
                .bic("CRBAALTRXXX")
                .build();

        when(bankRepository.findByBic(eq(SWIFTCODE))).thenReturn(entity);

        StepVerifier.create(bankService.getBank(SWIFTCODE)).
                assertNext(response ->
                        assertEquals(bank,
                                response))
                .verifyComplete();

        verify(bankRepository, times(1)).findByBic(eq(SWIFTCODE));
        verify(swiftClient, never()).getBankDetails(eq(SWIFTCODE));

    }

    @Test
    void test_find_bank_from_downstream_not_found() {

        doThrow(new DownStreamException(404, "not found")).when(swiftClient).getBankDetails(eq(SWIFTCODE));

        StepVerifier.create(bankService.getBank(SWIFTCODE))
                .expectErrorSatisfies(e -> assertThat(e).isInstanceOfSatisfying(DownStreamException.class,
                        problem -> {
                            assertEquals(NOT_FOUND.value(), problem.getCode());
                          //  assertEquals("[{\"logref\":\"error\",\"message\":\"User [none@adevinta.com] not found\",\"links\":[]}]",
                            //        problem.getBody());
                        }
                )).verify();




        verify(bankRepository, times(1)).findByBic(eq(SWIFTCODE));
        verify(swiftClient, times(1)).getBankDetails(eq(SWIFTCODE));

    }

}