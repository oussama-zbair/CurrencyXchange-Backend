package com.currencyxchange.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoLocationDTO {

    private String ip;
    private String country_name;
    private String country_code2;

    @JsonProperty("currency")
    private void unpackCurrency(Object currency) {
        if (currency instanceof java.util.Map map) {
            Object code = map.get("code");
            if (code instanceof String s) {
                this.currency = s;
            }
        }
    }

    private String currency;

    public String getCountryCode() {
        return country_code2;
    }

    public void setCountryCode(String countryCode) {
        this.country_code2 = countryCode;
    }

}
