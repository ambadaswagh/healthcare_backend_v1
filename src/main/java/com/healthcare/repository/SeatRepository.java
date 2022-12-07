package com.healthcare.repository;

import java.util.List;

import com.healthcare.model.entity.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long>, JpaSpecificationExecutor<Seat> {
    
    @Query(value = "select st from Seat st where st.table.id = ?1")
    List<Seat> findByTableId(Long tableId);

    @Query(value = "select st from Seat st where st.table.id = ?1")
    Page<Seat> findByTableIdPage(Long tableId, Pageable pageable);

    @Query(value = "select st from Seat st where st.table.id IN ?1")
    Page<Seat> findByTableIdList(List<Long> tabelIds, Pageable pageable);
}
