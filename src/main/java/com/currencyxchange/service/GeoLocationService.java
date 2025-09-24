package com.currencyxchange.service;

import com.currencyxchange.dto.CountryCurrencyDTO;
import com.currencyxchange.dto.GeoLocationDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class GeoLocationService {

    private final WebClient.Builder webClientBuilder;

    public GeoLocationService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * Fetches the user's geolocation info using IP API and assigns detected currency using /countries.
     */
    public Mono<GeoLocationDTO> getUserLocation(String ip) {
        String geoApiUrl = "http://ip-api.com/json/" + ip;
        return webClientBuilder.build()
                .get()
                .uri(geoApiUrl)
                .retrieve()
                .bodyToMono(GeoLocationDTO.class)
                .flatMap(location -> {
                    String countryCode = location.getCountryCode();
                    if (countryCode == null) {
                        location.setCurrency("USD");
                        return Mono.just(location);
                    }

                    // Fetch country-currency mapping dynamically
                    return getAllCountries().map(countries -> {
                        String matchedCurrency = countries.stream()
                                .filter(c -> c.getCountryCode().equalsIgnoreCase(countryCode))
                                .map(CountryCurrencyDTO::getCurrencyCode)
                                .findFirst()
                                .orElse("USD");

                        location.setCurrency(matchedCurrency);
                        return location;
                    });
                });
    }

    /**
     * Fetches all countries with currency and flag info using RestCountries API.
     */
    public Mono<List<CountryCurrencyDTO>> getAllCountries() {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://restcountries.com/v3.1")
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (CurrencyXchange)")
                .build();

        return webClient.get()
                .uri("/all?fields=name,cca2,currencies")
                .retrieve()
                .bodyToFlux(JsonNode.class)
                .map(json -> {
                    String countryName = json.path("name").path("common").asText("Unknown");
                    String countryCode = json.path("cca2").asText("N/A");

                    String currencyCode = "N/A";
                    String currencyName = "N/A";

                    JsonNode currencies = json.path("currencies");
                    if (currencies.fieldNames().hasNext()) {
                        String firstCode = currencies.fieldNames().next();
                        currencyCode = firstCode;
                        currencyName = currencies.path(firstCode).path("name").asText("Unknown");
                    }

                    return new CountryCurrencyDTO(countryName, currencyCode, currencyName, countryCode);
                })
                .collectList();
    }
}
