package com.healthcare.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Peter Trinh
 */
public interface GoogleApiService {
    public GeocodingResult getGeocoding(String address);
}
