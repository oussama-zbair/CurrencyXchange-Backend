package com.currencyxchange.service;

import com.currencyxchange.dto.CountryCurrencyDTO;
import com.currencyxchange.dto.GeoLocationDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GeoLocationService {

    private final WebClient.Builder webClientBuilder;

    public GeoLocationService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * Fetches the user's geolocation info using IP API.
     */
    public Mono<GeoLocationDTO> getUserLocation() {
        return webClientBuilder.build()
                .get()
                .uri("http://ip-api.com/json/")
                .retrieve()
                .bodyToMono(GeoLocationDTO.class)
                .map(location -> {
                    String currency = switch (location.getCountryCode()) {
                        case "US" -> "USD";
                        case "MA" -> "MAD";
                        case "FR" -> "EUR";
                        case "GB" -> "GBP";
                        case "IN" -> "INR";
                        default -> "USD";
                    };
                    location.setCurrency(currency);
                    return location;
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
