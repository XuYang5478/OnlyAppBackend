package com.xuyang.OnlyApp.repository;

import com.xuyang.OnlyApp.entity.covid.CovidCountryData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CovidDataRepository extends CrudRepository<CovidCountryData, Long> {

    List<CovidCountryData> findAllByCountryEnglishNameIsNot(String exclude);

    List<CovidCountryData> findAllByCountryEnglishNameAndProvinceEnglishNameIsNot(String country, String exclude_province);
}
