package com.xuyang.OnlyApp.entity;

import lombok.Data;

@Data
public class CovidData {
    private String ProvinceOrState;
    private String CountryOrRegion;
    private Double Latitude;
    private Double Longitude;
    private Long[] confirmed_numbers;
    private Long[] death_numbers;
    private Long[] recovered_numbers;

    public CovidData() {
        this.ProvinceOrState = "";
        this.CountryOrRegion = "";
        this.Latitude = 0.0;
        this.Longitude = 0.0;
        this.confirmed_numbers = new Long[]{(long) 0, (long) 0};
        this.death_numbers = new Long[]{(long) 0, (long) 0};
        this.recovered_numbers = new Long[]{(long) 0, (long) 0};
    }
}
