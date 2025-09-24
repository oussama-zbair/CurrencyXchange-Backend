package com.currencyxchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GeoLocationDTO {
    private String ip;

    @JsonProperty("country_name")
    private String country;

    @JsonProperty("country_code2")
    private String countryCode;

    private String currency;
}
