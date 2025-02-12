package com.currencyxchange.service;


import com.currencyxchange.dto.CurrencyDTO;
import com.currencyxchange.model.CurrencyResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CurrencyService {

    private final WebClient webClient;

    @Value("${api.exchangerate.key}")
    private String apiKey;

    public CurrencyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://v6.exchangerate-api.com/v6").build();
    }

    public Mono<CurrencyDTO> getExchangeRates(String baseCurrency) {
        return webClient.get()
                .uri("/{apiKey}/latest/{baseCurrency}", apiKey, baseCurrency)
                .retrieve()
                .bodyToMono(CurrencyResponse.class)
                .map(response -> new CurrencyDTO(response.getBase_code(), response.getConversion_rates()));
    }
}

