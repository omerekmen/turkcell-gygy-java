package com.turkcell.library_cqrs.domain.entity;

import java.util.UUID;
import java.util.List;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "sections")
public class Section {
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "floor_id")
    private Floor floor;

    @Column(name = "name", length = 100)
    private String name;

    @OneToMany(mappedBy = "section")
    private List<Shelve> shelves;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Shelve> getShelves() {
        return shelves;
    }

    public void setShelves(List<Shelve> shelves) {
        this.shelves = shelves;
    }
}
