package com.currencyxchange.service;

import com.currencyxchange.dto.CountryCurrencyDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class CountryCurrencyService {

    private final WebClient webClient;

    public CountryCurrencyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://restcountries.com/v3.1")
                .build();
    }

    public Flux<CountryCurrencyDTO> getAllCountriesWithCurrency() {
        return webClient.get()
                .uri("/all?fields=name,cca2,currencies")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Map.class)
                .flatMap(map -> {
                    try {
                        Map<String, Object> nameMap = (Map<String, Object>) map.get("name");
                        String country = nameMap.get("common").toString();

                        String countryCode = map.get("cca2").toString();
                        Map<String, Map<String, String>> currencies = (Map<String, Map<String, String>>) map.get("currencies");

                        if (currencies == null || currencies.isEmpty()) return Flux.empty();

                        String currencyCode = currencies.keySet().iterator().next();
                        String currencyName = currencies.get(currencyCode).get("name");

                        CountryCurrencyDTO dto = new CountryCurrencyDTO(
                                country,
                                currencyCode,
                                currencyName,
                                countryCode
                        );

                        return Flux.just(dto);

                    } catch (Exception e) {
                        System.err.println("Mapping error: " + e.getMessage());
                        return Flux.empty();
                    }
                });
    }


}
