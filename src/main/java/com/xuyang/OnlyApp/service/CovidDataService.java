package com.xuyang.OnlyApp.service;

import com.xuyang.OnlyApp.entity.CovidData;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
@Slf4j
public class CovidDataService {
//    private static final String CONFIRMED_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static final String CONFIRMED_DATA_URL = "https://cdn.jsdelivr.net/gh/CSSEGISandData/COVID-19@master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
//    private static final String DEATH_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";
    private static final String DEATH_DATA_URL = "https://cdn.jsdelivr.net/gh/CSSEGISandData/COVID-19@master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";
//    private static final String RECOVERED_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";
    private static final String RECOVERED_DATA_URL = "https://cdn.jsdelivr.net/gh/CSSEGISandData/COVID-19@master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";


    private static final String CONFIRMED="CONFIRMED";
    private static final String DEATH="DEATH";
    private static final String RECOVERED="RECOVERED";

    private final OkHttpClient client = new OkHttpClient();

    private final HashMap<String, CovidData> global_data = new HashMap<>();
    private final HashMap<String, CovidData> China_data = new HashMap<>();
    private final HashMap<String, Long> China_total_data = new HashMap<>();
    private final HashMap<String, Long> global_total_data = new HashMap<>();

    private Date updateDate = new Date();

    private void fetchData(String url, String item) throws IOException {
        Request request = new Request.Builder().url(url).build();
        String result = "";
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            log.error(e.toString());
        }
        if ("".equals(result)) {
            log.warn("未获取到"+item+"数据");
            return;
        }

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new StringReader(result));
        for (CSVRecord record : records) {
            String provinceOrState = record.get(0);
            String countryOrRegion = record.get(1);
            String key = provinceOrState + "," + countryOrRegion;

            ArrayList<Long> numbers = new ArrayList<>();
            for (int x = 31; x > 0; x--) {
                int i = record.size() - x;
                numbers.add(Long.parseLong("".equals(record.get(i)) ? "0" : record.get(i)));
            }

            CovidData data = global_data.getOrDefault(key, new CovidData());
            data.setProvinceOrState(provinceOrState);
            data.setCountryOrRegion(countryOrRegion);
            data.setLatitude(Double.parseDouble("".equals(record.get(2)) ? "0" : record.get(2)));
            data.setLongitude(Double.parseDouble("".equals(record.get(3)) ? "0" : record.get(3)));
            int current_date= record.size()-1;
            switch (item) {
                case CONFIRMED:
                    data.setConfirmed_numbers(numbers.toArray(new Long[0]));
                    data.setCurrent_confirmed(Long.parseLong("".equals(record.get(current_date))?"0":record.get(current_date)));
                    break;
                case DEATH:
                    data.setDeath_numbers(numbers.toArray(new Long[0]));
                    data.setCurrent_death(Long.parseLong("".equals(record.get(current_date))?"0":record.get(current_date)));
                    break;
                case RECOVERED:
                    data.setRecovered_numbers(numbers.toArray(new Long[0]));
                    data.setCurrent_recovered(Long.parseLong("".equals(record.get(current_date))?"0":record.get(current_date)));
                    break;
                default:
                    log.error("在\"CovidDataService-fetchData\"中，未知字段：" + item + "\n Url: " + url);
                    break;
            }
            global_data.put(key, data);
            if ("China".equals(countryOrRegion))
                China_data.put(key, data);
        }
        log.info("获取到" + item + "数据");
//        HttpClient client = HttpClient.newHttpClient();
//        HttpResponse<String> response = client.send(HttpRequest.newBuilder(URI.create(url)).build(), HttpResponse.BodyHandlers.ofString());
//        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new StringReader(response.body()));

    }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    private void updateData() throws IOException, InterruptedException {
        fetchData(CONFIRMED_DATA_URL, CONFIRMED);
        fetchData(DEATH_DATA_URL, DEATH);
        fetchData(RECOVERED_DATA_URL, RECOVERED);

        global_total_data.put(CONFIRMED, global_data.values().stream().mapToLong(data -> data.getCurrent_confirmed()).sum());
        global_total_data.put(DEATH, global_data.values().stream().mapToLong(data -> data.getCurrent_death()).sum());
        global_total_data.put(RECOVERED, global_data.values().stream().mapToLong(data -> data.getCurrent_recovered()).sum());

        China_total_data.put(CONFIRMED, China_data.values().stream().mapToLong(data -> data.getCurrent_confirmed()).sum());
        China_total_data.put(DEATH, China_data.values().stream().mapToLong(data -> data.getCurrent_death()).sum());
        China_total_data.put(RECOVERED, China_data.values().stream().mapToLong(data -> data.getCurrent_recovered()).sum());

        updateDate = new Date();
    }

    public CovidData[] getGlobalDetailData() {
        return global_data.values().toArray(new CovidData[0]);
    }

    public CovidData[] getChinaDetailData() {
        return China_data.values().toArray(new CovidData[0]);
    }

    public Map<String, Long> getGlobalTotalData() {
        return global_total_data;
    }

    public Map<String, Long> getChinaTotalData() {
        return China_total_data;
    }

    public String getUpdateTime() {
        return String.valueOf(updateDate.getTime());
    }
}
