package com.xuyang.OnlyApp.entity.covid;

import lombok.Data;

@Data
public class CovidGlobalTotalData {
    private long currentConfirmedCount;
    private long confirmedCount;
    private long curedCount;
    private long deadCount;
    private long currentConfirmedIncr;
    private long confirmedIncr;
    private long curedIncr;
    private long deadIncr;
    private long yesterdayConfirmedCountIncr;
}
