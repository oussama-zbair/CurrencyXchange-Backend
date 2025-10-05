package com.currencyxchange.service;

import com.currencyxchange.dto.CountryCurrencyDTO;
import com.currencyxchange.dto.GeoLocationDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
public class GeoLocationService {

    private final WebClient webClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public GeoLocationService(
            WebClient.Builder webClientBuilder,
            @Value("${IPGEO_API_KEY:${ipgeo.apiKey:}}") String apiKey) {

        this.webClient = webClientBuilder.build();
        this.apiKey = apiKey;
        if (this.apiKey == null || this.apiKey.isBlank()) {
            log.warn("IPGEO_API_KEY / ipgeo.apiKey is NOT set. Geolocation will fail.");
        }
    }

    private final String apiKey;

    public Mono<GeoLocationDTO> getUserLocation(String ip) {
        if (apiKey == null || apiKey.isBlank()) {
            return Mono.error(new IllegalStateException("Missing ipgeolocation API key"));
        }


        String url = "https://api.ipgeolocation.io/ipgeo?apiKey=" + apiKey + "&ip=" + ip;


        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(body -> {
                    try {
                        JsonNode root = mapper.readTree(body);

                        GeoLocationDTO dto = new GeoLocationDTO();
                        dto.setIp(root.path("ip").asText(null));

                        String cc2 = root.path("country_code2").asText(null);
                        if (cc2 == null || cc2.isBlank()) cc2 = root.path("countryCode").asText(null);
                        dto.setCountryCode(cc2);

                        String countryName = root.path("country_name").asText(null);
                        if (countryName == null || countryName.isBlank())
                            countryName = root.path("country").asText(null);
                        dto.setCountry_name(countryName);

                        // currency may be "string" or { code: "USD", ... }
                        String currencyCode = null;
                        JsonNode currNode = root.path("currency");
                        if (currNode.isObject()) {
                            currencyCode = currNode.path("code").asText(null);
                        } else if (currNode.isTextual()) {
                            currencyCode = currNode.asText(null);
                        }

                        if (currencyCode == null || currencyCode.isBlank()) {
                            String ccForMap = cc2 == null ? "" : cc2;
                            return getAllCountries().map(countries -> {
                                String matched = countries.stream()
                                        .filter(c -> ccForMap.equalsIgnoreCase(c.getCountryCode()))
                                        .map(CountryCurrencyDTO::getCurrencyCode)
                                        .findFirst().orElse("USD");
                                dto.setCurrency(matched);
                                return dto;
                            });
                        } else {
                            dto.setCurrency(currencyCode);
                            return Mono.just(dto);
                        }
                    } catch (Exception e) {
                        log.error("Failed to parse geolocation payload", e);
                        return Mono.error(new RuntimeException("Failed to parse geolocation payload"));
                    }
                })
                .onErrorResume(ex -> {
                    log.error("Geolocation lookup failed: {}", ex.toString());
                    GeoLocationDTO fallback = new GeoLocationDTO();
                    fallback.setCurrency("USD");
                    return Mono.just(fallback);
                });
    }

    public Mono<List<CountryCurrencyDTO>> getAllCountries() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("countries.json")) {
            if (in == null) return Mono.error(new RuntimeException("countries.json not found"));
            List<CountryCurrencyDTO> list = mapper.readValue(in, new TypeReference<>() {
            });
            return Mono.just(list);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to load country data", e));
        }
    }
}
