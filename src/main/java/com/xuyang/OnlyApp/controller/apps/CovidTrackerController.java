package com.xuyang.OnlyApp.controller.apps;

import com.xuyang.OnlyApp.entity.covid.CovidCountryData;
import com.xuyang.OnlyApp.entity.covid.CovidGlobalTotalData;
import com.xuyang.OnlyApp.entity.covid.CovidTotalData;
import com.xuyang.OnlyApp.service.CovidDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/covid")
public class CovidTrackerController {

    private CovidDataService covidDataService;

    public CovidTrackerController(CovidDataService covidDataService) {
        this.covidDataService = covidDataService;
    }

    @GetMapping("/update_time")
    public long getUpdateTime() {
        return covidDataService.getUpdateTime();
    }

    @GetMapping("/global_detail")
    public List<CovidCountryData> getGlobalData() {
        return covidDataService.getGlobalData();
    }

    @GetMapping("/China_detail")
    public List<CovidCountryData> getChinaData() {
        return covidDataService.getChinaData();
    }

    @GetMapping("/total")
    public CovidTotalData getChinaTotalData() {
        return covidDataService.getTotalData();
    }
}
