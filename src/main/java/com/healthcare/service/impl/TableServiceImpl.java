package com.healthcare.service.impl;

import com.healthcare.model.entity.TableEntity;
import com.healthcare.repository.TableRepository;
import com.healthcare.service.TableService;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.api.model.SeatDTO;
import com.healthcare.api.model.TableArrangementResponseDTO;
import com.healthcare.api.model.TableSeatDTO;
import com.healthcare.model.entity.Seat;
import com.healthcare.model.entity.TableEntity;
import com.healthcare.repository.TableRepository;
import com.healthcare.service.SeatService;
import com.healthcare.service.TableService;

import io.jsonwebtoken.lang.Collections;

@Service
@Transactional
public class TableServiceImpl extends BasicService<TableEntity, TableRepository> implements TableService {
    private static final String KEY = TableEntity.class.getSimpleName();

    @Autowired
    TableRepository repository;

    @Autowired SeatService seatService;


    @Autowired
    private RedisTemplate<String, TableEntity> redisTemplate;

    @Override
    @Transactional
    public List<TableEntity> findAll() {
        Map<Object, Object> tableEntityMap = redisTemplate.opsForHash().entries(KEY);
        List<TableEntity> tableEntityList = Collections.arrayToList(tableEntityMap.values().toArray());
        if (tableEntityMap.isEmpty()) {
            tableEntityList = repository.findAll();
        }
        return tableEntityList;
    }
    
    @Override
    @Transactional
    public TableEntity save(TableEntity tableEntity) {
        if (tableEntity.getId() == null) {
            tableEntity.setCreatedAt(new Timestamp(new Date().getTime()));
        } else {
            tableEntity.setUpdatedAt(new Timestamp(new Date().getTime()));
        }
        tableEntity = repository.save(tableEntity);
        redisTemplate.opsForHash().put(KEY, tableEntity.getId(), tableEntity);
        return tableEntity;
    }

    @Override @Transactional
    public TableEntity findById(Long id) {
        /*Object table = redisTemplate.opsForHash().get(KEY, id);
        if (table != null) {
            return (TableEntity) table;
        }*/
        return repository.findOne(id);
    }

    @Override
    public Long deleteById(Long id) {
        repository.delete(id);
        return redisTemplate.opsForHash().delete(KEY, id);
    }

    @Override
    public Long disableById(Long id) {
        return null;
    }

    @Override
    public List<TableEntity> findByAgencyId(Long agencyId) {
        return repository.findByAgencyId(agencyId);
    }

    @Override
    public List<TableEntity> finAllTablesAvailable() {
        return repository.finAllTablesAvailable();
    }
    
    @Override
    public TableArrangementResponseDTO getTableArrangementByAgency(Long agencyId) {
        TableArrangementResponseDTO tableArrangementResponseDTO = new TableArrangementResponseDTO();
        List<TableSeatDTO> tableSeatList = new ArrayList<>();
        
        List<TableEntity> tableList = repository.findByAgencyId(agencyId);
        int totalTables = tableList.size();
        
        for (TableEntity tableEntity : tableList) {
            List<SeatDTO> seatList = new ArrayList<>();
            TableSeatDTO tableSeatDTO = new TableSeatDTO();
            List<Seat> seats = seatService.findByTableId(tableEntity.getId());
            int totalSeats = seats.size();
            tableSeatDTO.setTotalSeats(totalSeats);
            
            for (Seat seat : seats) {
                SeatDTO seatDTO = new SeatDTO();
                seatDTO.setSeatId(seat.getId());
                seatDTO.setAvailable((seat.getStatus().toString().equals("AVAILABLE")) ? true : false);
                seatList.add(seatDTO);
            }
            tableSeatDTO.setSeats(seatList);
            tableSeatList.add(tableSeatDTO);
        }
        tableArrangementResponseDTO.setTables(tableSeatList);
        tableArrangementResponseDTO.setTotalTables(totalTables);
        return tableArrangementResponseDTO;
    }


    @Override
    public Page<TableEntity> findByAgency(Long agencyId, Pageable pageable) {
        return repository.findByAgency(agencyId, pageable);
    }

    @Override
    public List<TableEntity> findByAgency(Long agencyId) {
        return repository.findByAgency(agencyId);
    }

    @Override
    public Page<TableEntity> findByAgencyIds(List<Long> agencyIds, Pageable pageable) {
        return repository.findByAgencyIds(agencyIds, pageable);
    }

    @Override
    public List<TableEntity> findByAgencyIds(List<Long> agencyIds) {
        return repository.findByAgencyIds(agencyIds);
    }

    @Override
    @Transactional
    public List<TableEntity> finAllTablesAvailableByAgency(Long agencyId) {
        return repository.findByAgencyId(agencyId);
    }

    @Override
    @Transactional
    public List<TableEntity> finAllTablesAvailableByAgencyList(List<Long> agencyIds) {
        return repository.finAllTablesAvailableByAgencyList(agencyIds);
    }
}
