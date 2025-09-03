package com.realestate.app.repository;

import com.realestate.app.model.FlatEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlatRepository extends JpaRepository<FlatEntity, Long> {
    Page<FlatEntity> findAllByComplexId(Long complexId, PageRequest pageRequest);

    Optional<FlatEntity> findByComplexIdAndBuildingAndNumber(Long complexId, String building, String number);
}
