package com.example.hibernateweather.service;

import com.example.hibernateweather.dto.WeatherGeneralDTO;
import com.example.hibernateweather.entity.Weather;
import com.example.hibernateweather.repository.WeatherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@EnableScheduling
public class WeatherService {

    private final WeatherRepo repo;
    private final RestTemplate template;

    private String city;
    private String country;

    private final String KEY_WEATHER = "c1f5ebb5d2b044dfbe4994f7d4f7594a";
    private final String HOST = "https://api.weatherbit.io/v2.0/current?";

    @Autowired
    public WeatherService(WeatherRepo repo) {
        this.repo = repo;
        this.template = new RestTemplate();
    }

    public Weather initializeDateAndRun(String city, String country) {
        this.city = city;
        this.country = country;
        return getInfoFromWeatherAPI();
    }

    @Scheduled(cron = "0 0/15 * * * *")
    private Weather getInfoFromWeatherAPI() {
        if (city == null && country == null)
            return null;
        Weather weather = new Weather();

        System.out.println("Method Weather is call now");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(HOST)
                .queryParam("city", city)
                .queryParam("country", country)
                .queryParam("key", KEY_WEATHER);

        WeatherGeneralDTO json = template.getForObject(builder.toUriString(), WeatherGeneralDTO.class);
        assert json != null;
        weather = json.getData().get(0).toWeather();
        repo.save(weather);
        return weather;
    }

    public Page<Weather> getWeatherByCity(String city, Pageable pageable) {
        return repo.findAllByCity(city, pageable);
    }

    public Page<Weather> getAllWeather(Pageable pageable) {
        return repo.findAll(pageable);
    }
}
