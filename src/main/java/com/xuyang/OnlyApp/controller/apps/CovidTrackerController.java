package com.xuyang.OnlyApp.controller.apps;

import com.xuyang.OnlyApp.entity.CovidData;
import com.xuyang.OnlyApp.service.CovidDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/covid")
public class CovidTrackerController {

    private CovidDataService covidDataService;

    @Autowired
    public CovidTrackerController(CovidDataService covidDataService) {
        this.covidDataService = covidDataService;
    }

    @GetMapping("/update_time")
    public String getUpdateTime(){
        return covidDataService.getUpdateTime();
    }

    @GetMapping("/global_total")
    public Map<String, Long> getGlobalTotalData(){
        return covidDataService.getGlobalTotalData();
    }

    @GetMapping("/China_total")
    public Map<String, Long> getChinaTotalData(){
        return covidDataService.getChinaTotalData();
    }

    @GetMapping("/global_detail")
    public Map<String, CovidData> getGlobalDetailData(){
        return covidDataService.getGlobalDetailData();
    }

    @GetMapping("/China_detail")
    public Map<String, CovidData> getChinaDetailData(){
        return covidDataService.getChinaDetailData();
    }
}
