package com.healthcare.api;


import com.healthcare.model.entity.assessment.AssessmentUser;
import com.healthcare.model.entity.assessment.MonthlyIncome;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.repository.AssessmentUserRepository;
import com.healthcare.repository.assessment.MonthlyIncomeRepository;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/monthlyIncome")
public class MonthlyIncomeController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MonthlyIncomeRepository monthlyIncomeRepository;

    @Autowired
    AssessmentUserRepository assessmentUserRepository;

    @ApiOperation(value = "save monthlyIncome", notes = "save monthlyIncome")
    @ApiParam(name = "monthlyIncome", value = "monthlyIncome to save", required = true)
    @PostMapping()
    public ResponseEntity<MonthlyIncome> create(@RequestBody MonthlyIncome monthlyIncome) {
        monthlyIncome = monthlyIncomeRepository.save(monthlyIncome);
        saveAssessmentUser(monthlyIncome);
        return new ResponseEntity<MonthlyIncome>(monthlyIncome, HttpStatus.OK);
    }

    private void saveAssessmentUser(@RequestBody MonthlyIncome monthlyIncome) {
        AssessmentUser assessmentUser = assessmentUserRepository.findOne(monthlyIncome.getUserId());
        if(assessmentUser != null){
            assessmentUser.setMonthlyIncome(monthlyIncome);
            assessmentUserRepository.save(assessmentUser);
        }
    }

    @ApiOperation(value = "get monthlyIncome by id", notes = "get monthlyIncome by id")
    @ApiImplicitParam(name = "id", value = "monthlyIncome id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public MonthlyIncome read(@PathVariable("id") Long id) {
        logger.info("id : " + id);
        List<MonthlyIncome> monthlyIncomeList = monthlyIncomeRepository.findByUserId(id);
        if(monthlyIncomeList.size() > 0) {
            return monthlyIncomeList.get(0);
        } else {
            return new MonthlyIncome();
        }
    }

}
