package com.currencyxchange.service;


import com.currencyxchange.dto.GeoLocationDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;

@Service
public class GeoLocationService {

    private final WebClient webClient;
    private final WebClient restCountriesClient;

    public GeoLocationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://ip-api.com/json").build();
        this.restCountriesClient = webClientBuilder.baseUrl("https://restcountries.com/v3.1/alpha").build();
    }

    public Mono<GeoLocationDTO> getUserLocation() {
        return webClient.get()
                .retrieve()
                .bodyToMono(GeoLocationDTO.class)
                .flatMap(response -> fetchCurrencyForCountry(response)
                        .map(currency -> {
                            response.setCurrency(currency);
                            return response;
                        })
                );
    }

    private Mono<String> fetchCurrencyForCountry(GeoLocationDTO geoLocation) {
        return restCountriesClient.get()
                .uri("/{countryCode}", geoLocation.getCountryCode())
                .retrieve()
                .bodyToMono(Map[].class) // Get the first result
                .map(response -> {
                    Map<String, Object> currencies = (Map<String, Object>) response[0].get("currencies");
                    if (currencies != null && !currencies.isEmpty()) {
                        return currencies.keySet().iterator().next(); // Get first currency key (e.g., "MAD")
                    }
                    return "USD"; // Default fallback
                });
    }
}