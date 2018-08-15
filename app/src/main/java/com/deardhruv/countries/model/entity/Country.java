
package com.deardhruv.countries.model.entity;

public class Country {

    // NOTE: There could have been more classes, I'm keeping it simple as per requirement.

    private String country;
    private String currency;
    private String language;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return " -- " + country + " -- " + currency + " -- " + language + " -- \n";
    }

}
