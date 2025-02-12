package com.currencyxchange.dto;


import java.util.Map;

public class CurrencyDTO {
    private String baseCurrency;
    private Map<String, Double> exchangeRates;

    public CurrencyDTO(String baseCurrency, Map<String, Double> exchangeRates) {
        this.baseCurrency = baseCurrency;
        this.exchangeRates = exchangeRates;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Map<String, Double> getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(Map<String, Double> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }
}
