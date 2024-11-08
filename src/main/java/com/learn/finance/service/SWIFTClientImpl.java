package com.learn.finance.service;

import com.learn.finance.config.DownStreamProperties;
import com.learn.finance.model.consumer.BankDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Consumer;


@Slf4j
@Service
@RequiredArgsConstructor
public class SWIFTClientImpl implements SWIFTClient {

    @Qualifier("swiftWebClient")
    private final WebClient swiftWebClient;
    private final DownStreamProperties properties;

    public Mono<List<BankDetails>> getBankDetails(final String swiftCode) {
        log.info("Retrieve bank details for SWIFT Code [{}]", swiftCode);
        return swiftWebClient.get()
                .uri("/api/swift/" + swiftCode)
                .headers(getAuthorisationHeaders())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<BankDetails>>() {
                });

    }

    private Consumer<HttpHeaders> getAuthorisationHeaders() {
        return httpHeaders -> {
            httpHeaders.add("x-rapidapi-key", properties.getApiKeyHeader());
            httpHeaders.add("x-rapidapi-host", properties.getHostHeader());
            httpHeaders.add("useQueryString", "true");
        };
    }

}
