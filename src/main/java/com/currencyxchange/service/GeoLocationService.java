package com.currencyxchange.service;

import com.currencyxchange.dto.CountryCurrencyDTO;
import com.currencyxchange.dto.GeoLocationDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
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
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = new ClassPathResource("countries.json").getInputStream();
            List<CountryCurrencyDTO> countries = mapper.readValue(inputStream, new TypeReference<>() {});
            return Mono.just(countries);
        } catch (IOException e) {
            return Mono.error(new RuntimeException("Failed to load country data", e));
        }
    }
}
