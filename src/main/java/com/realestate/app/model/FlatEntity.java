package com.realestate.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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

    @Column(name = "actual_price_total")
    private int actualPriceTotal;

    @Column(name = "actual_price_per_m2")
    private int actualPricePerM2;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
}
