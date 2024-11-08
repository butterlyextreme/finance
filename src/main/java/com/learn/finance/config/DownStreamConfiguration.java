package com.learn.finance.config;

import com.learn.finance.exception.DownStreamException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class DownStreamConfiguration {

    private final DownStreamProperties downstreamProperties;

    public static ExchangeFilterFunction errorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (!clientResponse.statusCode().is2xxSuccessful()) {
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new DownStreamException(clientResponse.statusCode().value(), errorBody)));
            } else {
                return Mono.just(clientResponse);
            }
        });
    }

    @Bean
    public WebClient swiftWebClient(final WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(downstreamProperties.getClient().getUrl())
                .filter(errorHandler())
                .build();
    }

}
