package com.currencyxchange.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import service.CurrencyService;

import java.util.Map;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/rates")
    public Mono<Map> getExchangeRates(@RequestParam String baseCurrency) {
        return currencyService.getExchangeRates(baseCurrency);
    }
}
