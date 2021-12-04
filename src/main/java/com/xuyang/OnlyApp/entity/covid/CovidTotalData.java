package com.xuyang.OnlyApp.entity.covid;

import lombok.Data;

@Data
public class CovidTotalData {
    private long currentConfirmedCount;
    private long currentConfirmedIncr;
    private long confirmedCount;
    private long confirmedIncr;
    private long suspectedCount;
    private long suspectedIncr;
    private long curedCount;
    private long curedIncr;
    private long deadCount;
    private long deadIncr;
    private long seriousCount;
    private long seriousIncr;
    private long updateTime;
    private CovidGlobalTotalData globalStatistics;
}
