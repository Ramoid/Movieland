package com.solovey.movieland.dao.enums;


public enum Currency {
    UAH("UAH"),
    USD("USD"),
    EUR("EUR");
    private final String currency;

    Currency(String type) {
        this.currency = type;
    }

    public static Currency getCurrency(String currencyString) {

        for (Currency currency : Currency.values()) {
            if (currency.currency.equalsIgnoreCase(currencyString)) {
                return currency;
            }
        }
        //throw new IllegalArgumentException("Wrong currency");
        // UAH in case when wrong parameter value
        return Currency.UAH;
    }

    public String getCurrency(){
        return  currency;
    }
}
