package service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;

@Service
public class CurrencyService {

    private final WebClient webClient;

    // Inject API Key from application.properties
    @Value("${api.exchangerate.key}")
    private String apiKey;

    public CurrencyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://v6.exchangerate-api.com/v6").build();
    }

    public Mono<Map> getExchangeRates(String baseCurrency) {
        return webClient.get()
                .uri("/{apiKey}/latest/{baseCurrency}", apiKey, baseCurrency)
                .retrieve()
                .bodyToMono(Map.class);
    }
}
