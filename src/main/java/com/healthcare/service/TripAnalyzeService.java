package com.healthcare.service;

import com.healthcare.model.entity.TripAnalyze;

import java.util.List;
import java.util.Map;

public interface TripAnalyzeService extends IService<TripAnalyze>, IFinder<TripAnalyze> {
    public List<Map<String, String>> findDriverFrequency(Long userId);
}
