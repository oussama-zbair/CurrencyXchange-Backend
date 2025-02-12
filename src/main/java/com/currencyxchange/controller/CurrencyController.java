package com.currencyxchange.controller;

import com.currencyxchange.dto.CurrencyDTO;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.currencyxchange.service.CurrencyService;



@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/rates")
    public Mono<CurrencyDTO> getExchangeRates(@RequestParam String baseCurrency) {
        return currencyService.getExchangeRates(baseCurrency);
    }
}
