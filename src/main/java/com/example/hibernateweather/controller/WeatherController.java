package com.example.hibernateweather.controller;

import com.example.hibernateweather.entity.Weather;
import com.example.hibernateweather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping
public class WeatherController {

    private final WeatherService service;

    @Autowired
    public WeatherController(WeatherService service) {
        this.service = service;
    }

    @GetMapping("/{city}&{country}")
    public String getWeatherByCityAndCountry(@PathVariable("city") String city,
                                             @PathVariable("country") String country, Model model) {
        Weather weather = service.initializeDateAndRun(city, country);
        model.addAttribute("weather", weather);
        return "weather";
    }

    @GetMapping("/")
    public String getWeatherByCityFromDB(
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            Model model) {


        Page<Weather> weatherPage;
        List<Weather> weatherList;

        Pageable pageable = PageRequest.of(page - 1, 10);
        if (city != null) {
            weatherPage = service.getWeatherByCity(city, pageable);
        } else
            weatherPage = service.getAllWeather(pageable);

        weatherList = weatherPage.getContent();

        model.addAttribute("city", city);
        model.addAttribute("weatherList", weatherList);
        model.addAttribute("currentPage", weatherPage.getNumber());
        model.addAttribute("totalItems", weatherPage.getTotalElements());
        model.addAttribute("totalPages", weatherPage.getTotalPages());

        return "index";
    }

    @GetMapping("/test")
    public String criteriaByCountry(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            Model model) {

        Page<Weather> weatherPage;
        List<Weather> weatherList;

//        Pageable pageable = PageRequest.of(page - 1, 10);

//        weatherPage = service.getWeatherByCountry("RU", pageable);
        Object[] obj = service.getWeatherByCountry("UA", page);

        int totalPages = (int) obj[0];
        weatherList = (List<Weather>) obj[1];

        model.addAttribute("city", null);
        model.addAttribute("weatherList", weatherList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalItems", weatherList.size());
        model.addAttribute("totalPages", totalPages);
        return "index";
    }
}