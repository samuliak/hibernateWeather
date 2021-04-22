package com.example.hibernateweather.controller;


import com.example.hibernateweather.entity.Man;
import com.example.hibernateweather.entity.Women;
import com.example.hibernateweather.repository.ManyToManyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashSet;
import java.util.Set;

@Controller
@RequestMapping("/manytomany")
public class ManyToManyController {

    private final ManyToManyRepo repo;

    @Autowired
    public ManyToManyController(ManyToManyRepo repo) {
        this.repo = repo;
    }


    @GetMapping("/man/{id}")
    public String getMenWomenById(@PathVariable("id") int id, Model model) {
        Man man = repo.findById(id).get();
        model.addAttribute("man", man);
        return "test";
    }


    @GetMapping("/man")
    public String addManStaticName(Model model) {
        Man man = new Man();
        man.setName("TestFromController");
        Women women = new Women();
        women.setName("Masha");
        Set<Women> set = man.getWomenSet();
        set.add(women);
        man.setWomenSet(set);
        repo.save(man);
        model.addAttribute("man", man);
        return "test";
    }


}
