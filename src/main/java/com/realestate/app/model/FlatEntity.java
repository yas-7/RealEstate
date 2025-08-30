package com.realestate.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "flats")
@Setter
@Getter
public class FlatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "complex_id")
    private long complexId;

    private String building;

    private String number;

    private int floor;

    private int rooms;

    @Column(name = "area_total")
    private double areaTotal;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDate createdAt;
}
