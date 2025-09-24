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

    private static final String API_KEY = "e37414b2eccb442a94fc315a5cfa7559";

    public GeoLocationService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<GeoLocationDTO> getUserLocation(String ip) {
        String geoApiUrl = "https://api.ipgeolocation.io/ipgeo?apiKey=" + API_KEY;

        // Only add IP param if valid IP is provided (from frontend)
        if (ip != null && !ip.equals("::1") && !ip.equals("0:0:0:0:0:0:0:1") && !ip.isBlank()) {
            geoApiUrl += "&ip=" + ip;
        }

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
