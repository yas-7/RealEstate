package com.realestate.app.service;

import com.realestate.app.dto.FlatCreateDTO;
import com.realestate.app.dto.FlatDTO;
import com.realestate.app.dto.FlatPageDTO;
import com.realestate.app.exception.ResourceNotFoundException;
import com.realestate.app.mapper.FlatMapper;
import com.realestate.app.model.FlatEntity;
import com.realestate.app.model.FlatPriceHistoryEntity;
import com.realestate.app.repository.FlatPriceHistoryRepository;
import com.realestate.app.repository.FlatRepository;
import com.realestate.app.util.FlatSortBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FlatService {
    private static final Logger LOG = LoggerFactory.getLogger(FlatService.class);
    private final FlatRepository flatRepository;
    private final FlatPriceHistoryRepository flatPriceHistoryRepository;

    @Autowired
    public FlatService(FlatRepository flatRepository, FlatPriceHistoryRepository flatPriceHistoryRepository) {
        this.flatRepository = flatRepository;
        this.flatPriceHistoryRepository = flatPriceHistoryRepository;
    }

    public FlatPageDTO getAllFlats(
            @Nullable Long complexId,
            int page,
            int size,
            FlatSortBy sortProperty,
            Sort.Direction sortDirection
    ) {
        Sort sort = sortProperty.getSort(sortDirection);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<FlatEntity> flatEntityPage = complexId == null ? flatRepository.findAll(pageRequest)
                : flatRepository.findAllByComplexId(complexId, pageRequest);

        return new FlatPageDTO(
                flatEntityPage.getContent().stream().map(FlatMapper::toDTO).toList(),
                flatEntityPage.getNumber(),
                flatEntityPage.isLast()
        );
    }

    public FlatDTO getFlat(long id) {
        FlatEntity flat = flatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flat", id));

        return FlatMapper.toDTO(flat);
    }

    public FlatDTO create(FlatCreateDTO dto) {
        FlatEntity flat = FlatMapper.toEntity(dto);

        flatRepository.save(flat);

        return FlatMapper.toDTO(flat);
    }

    public void delete(long flatId) {
        flatRepository.deleteById(flatId);
    }

    public void saveEntity(FlatEntity flatEntity) {
        Optional<FlatEntity> optionalFlat = flatRepository.findByComplexIdAndBuildingAndNumber(
                flatEntity.getComplexId(),
                flatEntity.getBuilding(),
                flatEntity.getNumber()
        );

        if (optionalFlat.isEmpty()) {
            FlatEntity savedFlat = flatRepository.save(flatEntity);
            LOG.info("Новая квартира сохранена: {}", flatEntity.getId());

            FlatPriceHistoryEntity historyEntity = new FlatPriceHistoryEntity();
            historyEntity.setFlatId(savedFlat.getId());
            historyEntity.setPriceTotal(savedFlat.getActualPriceTotal());
            historyEntity.setPricePerM2(savedFlat.getActualPricePerM2());

            flatPriceHistoryRepository.save(historyEntity);
        } else {
            FlatEntity existedFlat = optionalFlat.get();
            existedFlat.setActualPriceTotal(flatEntity.getActualPriceTotal());
            existedFlat.setActualPricePerM2(flatEntity.getActualPricePerM2());

            flatRepository.save(existedFlat);

            FlatPriceHistoryEntity historyEntity = new FlatPriceHistoryEntity();
            historyEntity.setFlatId(existedFlat.getId());
            historyEntity.setPriceTotal(existedFlat.getActualPriceTotal());
            historyEntity.setPricePerM2(existedFlat.getActualPricePerM2());

            flatPriceHistoryRepository.save(historyEntity);
        }
    }
}
