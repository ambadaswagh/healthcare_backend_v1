package com.healthcare.service;

import com.healthcare.model.entity.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SeatService extends IService<Seat>, IFinder<Seat> {
    List<Seat> findAll();

    List<Seat> findByTableId(Long tableId);

    Page<Seat> findByTable(Long tableId, Pageable pageable);

    Page<Seat> findByTableIdList(List<Long> tabelIds, Pageable pageable);
}
