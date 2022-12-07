package com.healthcare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.Meal;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long>, JpaSpecificationExecutor<Meal> {

    @Query(value = "select tr from Meal tr where tr.agency.id = ?1")
    Page<Meal> findAllByAgency(Long agencyId, Pageable pageable);

    @Query(value = "select tr from Meal tr where tr.agency.id = ?1 and tr.selected = 1")
    List<Meal> findAllByAgency(Long agencyId);

    @Query(value = "select tr from Meal tr where tr.agency.id in ?1")
    Page<Meal> findAllByAgencies(List<Long> agencies, Pageable pageable);
}
