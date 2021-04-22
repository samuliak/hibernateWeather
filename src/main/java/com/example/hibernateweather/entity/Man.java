package com.example.hibernateweather.entity;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "man")
public class Man {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "man_id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "man_woman",
            joinColumns = {@JoinColumn(name = "man_id")},
            inverseJoinColumns = {@JoinColumn(name = "woman_id")})
    Set<Women> womenSet = new HashSet<Women>();


    public Man() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Women> getWomenSet() {
        return womenSet;
    }

    public void setWomenSet(Set<Women> womenSet) {
        this.womenSet = womenSet;
    }
}