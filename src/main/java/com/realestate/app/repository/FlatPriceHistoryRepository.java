package com.realestate.app.repository;

import com.realestate.app.model.FlatPriceHistoryEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlatPriceHistoryRepository extends JpaRepository<FlatPriceHistoryEntity, Long> {
    List<FlatPriceHistoryEntity> findAllByFlatId(long flatId, PageRequest pageRequest);
}
