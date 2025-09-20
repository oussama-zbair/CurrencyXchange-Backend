package com.currencyxchange.service;

import com.currencyxchange.dto.CurrencyDTO;
import com.currencyxchange.model.CurrencyResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CurrencyService {

    private final WebClient.Builder webClientBuilder;

    @Value("${api.exchangerate.key}")
    private String apiKey;

    public CurrencyService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<CurrencyDTO> getExchangeRates(String baseCurrency) {
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + baseCurrency;

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(CurrencyResponse.class)
                .map(response -> new CurrencyDTO(
                        response.getBase_code(),
                        response.getConversion_rates()
                ));
    }
}
