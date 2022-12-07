package com.healthcare.service.impl;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.healthcare.service.GoogleApiService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by Peter Trinh.
 */
@Service
public class GoogleApiServiceImpl implements GoogleApiService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${map.api.key}")
    private String apiKey;

    public GeocodingResult getGeocoding(String address) {
        try {
            if (address == null || StringUtils.isEmpty(address) || StringUtils.equalsIgnoreCase(address,"null")) {
                return null;
            }
            GeoApiContext context = new GeoApiContext().setApiKey(apiKey);
            GeocodingResult[] results = GeocodingApi.geocode(context, address)
                    .language("en")
                    .awaitIgnoreError();
            return results == null ? null : results[0];
        } catch (Exception ex) {
            log.error(" getGeocoding error, " + ex.getMessage());
            return null;
        }
    }

}
