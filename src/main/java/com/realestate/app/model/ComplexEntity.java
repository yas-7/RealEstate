package com.realestate.app.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "complexes")
public class ComplexEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Nullable
    private String city;

    @Nullable
    private String address;

    @Nullable
    private String developer;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDate createdAt;
}
