package com.example.hibernateweather.repository;

import com.example.hibernateweather.entity.Weather;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface WeatherRepo extends CrudRepository<Weather, Integer> {

    Page<Weather> findAll(Pageable pageable);

    Page<Weather> findAllByCity(String city, Pageable pageable);

    Page<Weather> getWeatherByCountry(String city, Pageable pageable);
}
