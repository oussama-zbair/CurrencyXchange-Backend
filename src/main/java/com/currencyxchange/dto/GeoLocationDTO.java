package com.currencyxchange.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoLocationDTO {

    private String ip;

    @JsonProperty("country_name")
    private String country_name;

    @JsonProperty("country_code2")
    private String country_code2;

    private String currency;

    @JsonProperty("currency")
    private void unpackCurrency(Object currencyObj) {
        if (currencyObj instanceof java.util.Map<?, ?> map) {
            Object code = map.get("code");
            if (code instanceof String s) this.currency = s;
        } else if (currencyObj instanceof String s) {
            this.currency = s;
        }
    }

    public String getCountryCode() {
        return country_code2;
    }

    public void setCountryCode(String countryCode) {
        this.country_code2 = countryCode;
    }
}
