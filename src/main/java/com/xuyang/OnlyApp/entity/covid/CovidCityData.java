package com.xuyang.OnlyApp.entity.covid;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class CovidCityData {
    @Id
    private long locationId;
    private String cityName;
    private String cityEnglishName;
    private long currentConfirmedCount;
    private long confirmedCount;
    private long suspectedCount;
    private long curedCount;
    private long deadCount;
    private long highDangerCount;
    private long midDangerCount;
}
