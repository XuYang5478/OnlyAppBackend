package com.xuyang.OnlyApp.entity;

import lombok.Data;

@Data
public class CovidData {
    private String ProvinceOrState;
    private String CountryOrRegion;
    private Double Latitude;
    private Double Longitude;
    private long current_confirmed;
    private long current_death;
    private long current_recovered;
    private Long[] confirmed_numbers;
    private Long[] death_numbers;
    private Long[] recovered_numbers;
}
