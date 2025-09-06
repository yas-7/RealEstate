package com.realestate.app.repository;

import com.realestate.app.model.FlatEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlatRepository extends JpaRepository<FlatEntity, Long> {

    Optional<FlatEntity> findByComplexIdAndBuildingAndNumber(Long complexId, String building, String number);

    @Query(value = """
            SELECT f
            FROM FlatEntity f
            WHERE (:complexId IS NULL OR f.complexId = :complexId)
            AND (:building IS NULL OR f.building = :building)
            AND (:number IS NULL OR f.number = :number)
            """)
    Page<FlatEntity> findAllByComplexIdAndBuildingAndNumber(
            @Param("complexId") Long complexId,
            @Param("building") String building,
            @Param("number") String number,
            Pageable pageRequest
    );
}
