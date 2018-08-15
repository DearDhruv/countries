package com.deardhruv.countries.model;

import com.deardhruv.countries.model.entity.Country;

import java.util.List;

public class CountriesResponse {
    private List<Country> countries = null;

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Country country : countries) {
            stringBuilder.append(country.toString());
        }
        return stringBuilder.toString();
    }
}
