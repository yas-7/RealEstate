package com.realestate.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "flat_price_history")
@Getter
@Setter
public class FlatPriceHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "flat_id")
    private long flatId;

    @Column(name = "price_total")
    private int priceTotal;

    @Column(name = "price_per_m2")
    private int pricePerM2;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
