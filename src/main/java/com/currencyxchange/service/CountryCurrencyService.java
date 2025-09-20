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
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {})
                .flatMap(map -> {
                    try {
                        Map<String, Object> nameMap = (Map<String, Object>) map.get("name");
                        String country = nameMap != null ? nameMap.get("common").toString() : null;

                        String countryCode = map.get("cca2") != null ? map.get("cca2").toString() : null;

                        Map<String, Map<String, Object>> currencies =
                                (Map<String, Map<String, Object>>) map.get("currencies");

                        if (country == null || countryCode == null || currencies == null || currencies.isEmpty()) {
                            return Flux.empty(); // skip invalid
                        }

                        String currencyCode = currencies.keySet().iterator().next();
                        Map<String, Object> currencyDetails = currencies.get(currencyCode);
                        if (currencyDetails == null || !currencyDetails.containsKey("name")) {
                            return Flux.empty(); // skip
                        }

                        String currencyName = currencyDetails.get("name").toString();

                        return Flux.just(new CountryCurrencyDTO(
                                country, currencyCode, currencyName, countryCode
                        ));
                    } catch (Exception e) {
                        System.err.println("⚠️ Mapping error: " + e.getMessage());
                        return Flux.empty();
                    }
                });
    }


}
