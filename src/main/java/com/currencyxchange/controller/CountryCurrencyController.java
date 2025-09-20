package com.currencyxchange.controller;

import com.currencyxchange.dto.CountryCurrencyDTO;
import com.currencyxchange.service.CountryCurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryCurrencyController {

    private final CountryCurrencyService countryCurrencyService;

    @GetMapping
    public Flux<CountryCurrencyDTO> getAllCountriesWithCurrency() {
        return countryCurrencyService.getAllCountriesWithCurrency();
    }
}
