package com.realestate.app.repository;

import com.realestate.app.model.FlatEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlatRepository extends JpaRepository<FlatEntity, Long> {
    List<FlatEntity> findAllByComplexId(Long complexId, PageRequest pageRequest);
}
