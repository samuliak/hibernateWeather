package com.example.hibernateweather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherGeneralDTO {
    private List<WeatherDTO> data;

    private int count;

    public WeatherGeneralDTO() {
    }

    public List<WeatherDTO> getData() {
        return data;
    }

    public void setData(List<WeatherDTO> data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
