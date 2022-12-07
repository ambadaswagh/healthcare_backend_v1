package com.healthcare.service;

import java.util.List;

import com.healthcare.api.model.TableArrangementResponseDTO;
import com.healthcare.model.entity.TableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface TableService extends IService<TableEntity>, IFinder<TableEntity> {
    public List<TableEntity> findAll();
    List<TableEntity> findByAgencyId(Long agencyId);

    List<TableEntity> finAllTablesAvailable();
    
    TableArrangementResponseDTO getTableArrangementByAgency(Long agencyId);

    Page<TableEntity> findByAgency(Long agencyId, Pageable pageable);

    List<TableEntity> findByAgency(Long agencyId);

    Page<TableEntity> findByAgencyIds(List<Long> agencyIds, Pageable pageable);

    List<TableEntity> findByAgencyIds(List<Long> agencyIds);

    List<TableEntity> finAllTablesAvailableByAgency(Long agencyId);

    List<TableEntity> finAllTablesAvailableByAgencyList(List<Long> agencyIds);
}