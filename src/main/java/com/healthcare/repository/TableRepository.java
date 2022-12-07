package com.healthcare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.TableEntity;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long>, JpaSpecificationExecutor<TableEntity> {

    @Query(value = "select t from TableEntity t where t.agency.id = ?1")
    List<TableEntity> findByAgencyId(Long agencyId);

    @Query(value = "select t from TableEntity t where t.tableCapacity > ( select count(s.id) from Seat s where s.table = t)  ")
    List<TableEntity> finAllTablesAvailable();

    @Query(value = "select t from TableEntity t where t.agency.id = ?1")
    Page<TableEntity> findByAgency(Long agencyId, Pageable pageable);

    @Query(value = "select t from TableEntity t where t.agency.id = ?1")
    List<TableEntity> findByAgency(Long agencyId);

    @Query(value = "select t from TableEntity t where t.agency.id IN ?1")
    Page<TableEntity> findByAgencyIds(List<Long> agencyIds, Pageable pageable);

    @Query(value = "select t from TableEntity t where t.agency.id IN ?1")
    List<TableEntity> findByAgencyIds(List<Long> agencyIds);

    @Query(value = "select t from TableEntity t where t.agency.id = ?1 AND t.tableCapacity > ( select count(s.id) from Seat s where s.table = t)  ")
    List<TableEntity> finAllTablesAvailableByAgency(Long agencyId);

    @Query(value = "select t from TableEntity t where t.agency.id IN ?1 AND t.tableCapacity > ( select count(s.id) from Seat s where s.table = t)  ")
    List<TableEntity> finAllTablesAvailableByAgencyList(List<Long> agencyIds);
}