package com.currencyxchange.controller;

import com.currencyxchange.dto.CountryCurrencyDTO;
import com.currencyxchange.service.CountryCurrencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class CountryCurrencyController {

    private final CountryCurrencyService countryCurrencyService;

    public CountryCurrencyController(CountryCurrencyService countryCurrencyService) {
        this.countryCurrencyService = countryCurrencyService;
    }

    @GetMapping("/countries")
    public Flux<CountryCurrencyDTO> getCountries() {
        return countryCurrencyService.getAllCountriesWithCurrency();
    }
}
