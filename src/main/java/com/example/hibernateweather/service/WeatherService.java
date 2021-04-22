package com.example.hibernateweather.service;

import com.example.hibernateweather.dto.WeatherGeneralDTO;
import com.example.hibernateweather.entity.Weather;
import com.example.hibernateweather.repository.WeatherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@EnableScheduling
public class WeatherService {

    private final EntityManager em;
    private final WeatherRepo repo;
    private final RestTemplate template;

    private String city;
    private String country;

    private final String KEY_WEATHER = "c1f5ebb5d2b044dfbe4994f7d4f7594a";
    private final String HOST = "https://api.weatherbit.io/v2.0/current?";

    @Autowired
    public WeatherService(WeatherRepo repo, EntityManager em) {
        this.repo = repo;
        this.template = new RestTemplate();
        this.em = em;
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


    public Object[] getWeatherByCountry(String country, int page) {

        CriteriaBuilder builder = em.getCriteriaBuilder();


        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        countQuery.select(builder.count(countQuery.from(Weather.class)));
        Long count = em.createQuery(countQuery).getSingleResult();
        int totalItems = Math.toIntExact(count);
        int totalPages = (totalItems / 3 + ((totalItems % 3 == 0) ? 0 : 1));


        // getting pagination with predicate
        CriteriaQuery<Weather> query = builder.createQuery(Weather.class);
        Root<Weather> root = query.from(Weather.class);

        Predicate fromUkraine = builder.equal(root.get("country"), country);
        query.where(builder.and(fromUkraine));


        TypedQuery<Weather> typedQuery = em.createQuery(query.select(root));

        typedQuery.setFirstResult((page-1) * 3);
        typedQuery.setMaxResults(3);

        List<Weather> list = typedQuery.getResultList();
        Object [] obj = new Object[2];
        obj[0] = totalPages;
        obj[1] = list;

        return obj;
    }
}
