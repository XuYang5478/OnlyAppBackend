package com.xuyang.OnlyApp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuyang.OnlyApp.entity.covid.CovidTotalData;
import com.xuyang.OnlyApp.entity.covid.CovidCountryData;
import com.xuyang.OnlyApp.repository.CovidDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class CovidDataService {

    private CovidDataRepository covidDataRepository;

    public CovidDataService(CovidDataRepository covidDataRepository) {
        this.covidDataRepository = covidDataRepository;
    }

    private static String DETAIL_DATA_URL = "https://lab.isaaclin.cn/nCoV/api/area?latest=1";
    private static String TOTAL_DATA_URL = "https://lab.isaaclin.cn/nCoV/api/overall";
    private static CovidTotalData total_data = new CovidTotalData();
    private static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

//    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    private void fetchData() throws IOException {
        covidDataRepository.deleteAll();

        log.info("正在获取疫情数据……");

        List<CovidCountryData> country_data = new ArrayList<>();
        JsonNode root = mapper.readTree(new URL(DETAIL_DATA_URL));
        JsonNode results = root.get("results");
        if (results.isArray()) {
            country_data = mapper.readValue(results.traverse(), new TypeReference<List<CovidCountryData>>() {
            });
        }

        if (country_data.size() > 0) {
            AtomicLong location_id = new AtomicLong(1);
            for (CovidCountryData data : country_data) {
                //未没有locationId的地区分配id
                if(data.getLocationId()<=0)
                    data.setLocationId(location_id.getAndIncrement());

                if (data.getCities() != null) {
                    data.getCities().forEach((city) -> {
                        if (city.getLocationId()<=0)
                            city.setLocationId(location_id.getAndIncrement());
                    });
                }
                try {
                    CovidCountryData existed_data = covidDataRepository.findById(data.getLocationId()).orElse(null);
                    if (existed_data == null) {
                        covidDataRepository.save(data);
                    } else {
                        if (data.getUpdateTime().getTime() > existed_data.getUpdateTime().getTime())
                            covidDataRepository.save(data);
                    }
                } catch (Exception e) {
                    log.warn(e.getMessage());
                    log.warn("无法保存：" + data);
                }
            }
        } else {
            log.warn("未获取到疫情详细数据");
        }
        log.info("疫情详细数据加载完成");

        JsonNode total = mapper.readTree(new URL(TOTAL_DATA_URL));
        JsonNode result_total = total.get("results");
        if (result_total.isArray()) {
            for (JsonNode result : result_total) {
                total_data = mapper.readValue(result.traverse(), CovidTotalData.class);
            }
        } else {
            log.warn("未获取到疫情总体数据");
        }
        log.info("疫情总体数据加载完成");
    }

    public List<CovidCountryData> getGlobalData() {
        return covidDataRepository.findAllByCountryEnglishNameIsNot("China");
    }

    public List<CovidCountryData> getChinaData() {
        return covidDataRepository.findAllByCountryEnglishNameAndProvinceEnglishNameIsNot("China","China");
    }

    public CovidTotalData getTotalData() {
        return total_data;
    }

    public long getUpdateTime() {
        return total_data.getUpdateTime();
    }
}
