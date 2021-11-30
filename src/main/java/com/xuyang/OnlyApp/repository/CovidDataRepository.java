package com.xuyang.OnlyApp.repository;

import com.xuyang.OnlyApp.entity.CovidCountryData;
import org.springframework.data.repository.CrudRepository;

public interface CovidDataRepository extends CrudRepository<CovidCountryData, Long> {
}
