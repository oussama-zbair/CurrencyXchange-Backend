package com.currencyxchange.model;

import java.util.Map;

public class CurrencyResponse {
    private String base_code;
    private Map<String, Double> conversion_rates;

    // Getters and Setters
    public String getBase_code() {
        return base_code;
    }

    public void setBase_code(String base_code) {
        this.base_code = base_code;
    }

    public Map<String, Double> getConversion_rates() {
        return conversion_rates;
    }

    public void setConversion_rates(Map<String, Double> conversion_rates) {
        this.conversion_rates = conversion_rates;
    }
}
