package com.healthcare.repository;

import com.healthcare.model.entity.AgencyTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgencyTableRepository extends JpaRepository<AgencyTable, Long> {

}