package com.example.hibernateweather.entity;


import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "woman")
public class Women {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "woman_id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "womenSet")
    Set<Man> manSet = new LinkedHashSet<Man>();

    public Women() {
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

    public Set<Man> getManSet() {
        return manSet;
    }

    public void setManSet(Set<Man> manSet) {
        this.manSet = manSet;
    }
}
