package com.currencyxchange.dto;

public class CountryCurrencyDTO {

    private String countryName;
    private String currencyCode;
    private String currencyName;
    private String countryCode;

    public CountryCurrencyDTO() {}

    public CountryCurrencyDTO(String countryName, String currencyCode, String currencyName, String countryCode) {
        this.countryName = countryName;
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}

