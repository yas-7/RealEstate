package com.realestate.app.repository;

import com.realestate.app.model.ComplexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplexRepository extends JpaRepository<ComplexEntity, Long> {
}
