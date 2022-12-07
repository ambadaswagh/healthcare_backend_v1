package com.healthcare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.Training;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long>, JpaSpecificationExecutor<Training> {

    Training findById(long l);

    @Query(value = "select tr from Training tr where tr.agency.id = ?1")
    Page<Training> findAllByAgency(Long agencyId, Pageable pageable);

    @Query(value = "select tr from Training tr where tr.agency.id in ?1")
    Page<Training> findAllByAgencies(List<Long> agencies, Pageable pageable);
}