package com.healthcare.service;

import com.healthcare.model.entity.AgencyTable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AgencyTableService extends IService<AgencyTable> {
    public List<AgencyTable> findAll();
}