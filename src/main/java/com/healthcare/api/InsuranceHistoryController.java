package com.healthcare.api;


import com.healthcare.model.entity.InsuranceHistory;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.repository.InsuranceHistoryRepository;
import com.healthcare.service.InsuranceHistoryService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/insuranceHistory")
public class InsuranceHistoryController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private InsuranceHistoryService insuranceHistoryService;

    @Autowired
    private InsuranceHistoryRepository insuranceHistoryRepository;

    @ApiOperation(value = "save insuranceHistory", notes = "save insuranceHistory")
    @ApiParam(name = "riinsuranceHistoryde", value = "insuranceHistory to save", required = true)
    @PostMapping()
    public ResponseEntity<InsuranceHistory> create(@RequestBody InsuranceHistory insuranceHistory) {
        insuranceHistory = insuranceHistoryService.save(insuranceHistory);
        return new ResponseEntity<InsuranceHistory>(insuranceHistory, HttpStatus.OK);
    }

    @ApiOperation(value = "get insuranceHistory by id", notes = "get insuranceHistory by id")
    @ApiImplicitParam(name = "id", value = "insuranceHistory id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public InsuranceHistory read(@PathVariable Long id) {
        logger.info("id : " + id);
        return insuranceHistoryService.findById(id);
    }

    @ApiOperation(value = "get all insuranceHistory", notes = "get all insuranceHistory")
    @GetMapping()
    public ResponseEntity<Page<InsuranceHistory>> readAll(@RequestParam MultiValueMap<String, String> attributes) {
        MultiValueMapConverter<InsuranceHistory> converter = new MultiValueMapConverter<>(attributes, InsuranceHistory.class);
        return ResponseEntity.ok(insuranceHistoryService.findAll(converter.getData(), converter.getPageable()));
    }

    @ApiOperation(value = "get all insuranceHistory", notes = "get all insuranceHistory")
    @GetMapping("/getAllInsuranceByUser/{id}")
    public ResponseEntity<List<InsuranceHistory>> readAll(@PathVariable Long id) {
        return ResponseEntity.ok(insuranceHistoryService.findByUserId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> delete(@PathVariable("id") Long id) {
        logger.info("id : " + id);
        insuranceHistoryService.deleteById(id);
        return ResponseEntity.ok(1);
    }
}
