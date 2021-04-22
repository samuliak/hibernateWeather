package com.example.hibernateweather.repository;

import com.example.hibernateweather.entity.Man;
import org.springframework.data.repository.CrudRepository;

public interface ManyToManyRepo extends CrudRepository<Man, Integer> {
}
