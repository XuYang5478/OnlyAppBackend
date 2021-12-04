package com.xuyang.OnlyApp.entity.covid;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class CovidCountryData {
    @Id
    private long locationId;
    private String continentName;
    private String continentEnglishName;
    private String countryName;
    private String countryEnglishName;
    private String countryFullName;
    private String provinceName;
    private String provinceEnglishName;
    private String provinceShortName;
    private long currentConfirmedCount;
    private long confirmedCount;
    private long suspectedCount;
    private long curedCount;
    private long deadCount;
    private String comment;
    private Date updateTime;

    @OneToMany(targetEntity = CovidCityData.class, cascade = CascadeType.ALL)
    List<CovidCityData> cities;
}
