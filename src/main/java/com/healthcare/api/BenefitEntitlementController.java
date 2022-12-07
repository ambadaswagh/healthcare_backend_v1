package com.healthcare.api;


import com.healthcare.model.entity.assessment.AssessmentUser;
import com.healthcare.model.entity.assessment.BenefitEntitlement;
import com.healthcare.repository.AssessmentUserRepository;
import com.healthcare.repository.assessment.BenefitEntitlementRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/benefitEntitlement")
public class BenefitEntitlementController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BenefitEntitlementRepository benefitEntitlementRepository;

    @Autowired
    AssessmentUserRepository assessmentUserRepository;

    @ApiOperation(value = "save benefitEntitlement", notes = "save benefitEntitlement")
    @ApiParam(name = "benefitEntitlement", value = "benefitEntitlement to save", required = true)
    @PostMapping()
    public ResponseEntity<BenefitEntitlement> create(@RequestBody BenefitEntitlement benefitEntitlement) {
        benefitEntitlement = benefitEntitlementRepository.save(benefitEntitlement);
        saveAssessmentUser(benefitEntitlement);
        return new ResponseEntity<BenefitEntitlement>(benefitEntitlement, HttpStatus.OK);
    }

    private void saveAssessmentUser(@RequestBody BenefitEntitlement benefitEntitlement) {
        AssessmentUser assessmentUser = assessmentUserRepository.findOne(benefitEntitlement.getUserId());
        if(assessmentUser != null){
            assessmentUser.setBenefitEntitlement(benefitEntitlement);
            assessmentUserRepository.save(assessmentUser);
        }
    }

    @ApiOperation(value = "get benefitEntitlement by id", notes = "get benefitEntitlement by id")
    @ApiImplicitParam(name = "id", value = "benefitEntitlement id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public BenefitEntitlement read(@PathVariable Long id) {
        logger.info("id : " + id);
        List<BenefitEntitlement> monthlyIncomeList = benefitEntitlementRepository.findByUserId(id);
        if(monthlyIncomeList.size() > 0) {
            return monthlyIncomeList.get(0);
        } else {
            return new BenefitEntitlement();
        }
    }
}
