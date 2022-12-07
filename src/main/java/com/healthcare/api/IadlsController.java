package com.healthcare.api;


import com.healthcare.model.entity.assessment.AssessmentUser;
import com.healthcare.model.entity.assessment.Iadls;
import com.healthcare.repository.AssessmentUserRepository;
import com.healthcare.repository.assessment.IadlsRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping(value = "/api/iadls")
public class IadlsController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IadlsRepository iadlsRepository;

    @Autowired
    AssessmentUserRepository assessmentUserRepository;

    @ApiOperation(value = "save iadls", notes = "save iadls")
    @ApiParam(name = "iadls", value = "iadls to save", required = true)
    @PostMapping()
    public ResponseEntity<Iadls> create(@RequestBody Iadls iadls) {
        iadls = iadlsRepository.save(iadls);
        saveAssessmentUser(iadls);
        return new ResponseEntity<Iadls>(iadls, HttpStatus.OK);
    }

    private void saveAssessmentUser(@RequestBody Iadls iadls) {
        AssessmentUser assessmentUser = assessmentUserRepository.findOne(iadls.getUserId());
        if(assessmentUser != null){
            assessmentUser.setIadls(iadls);
            assessmentUserRepository.save(assessmentUser);
        }
    }

    @ApiOperation(value = "get iadls by id", notes = "get iadls by id")
    @ApiImplicitParam(name = "id", value = "iadls id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public Iadls read(@PathVariable("id") Long id) {
        logger.info("id : " + id);
        List<Iadls> iadlsList = iadlsRepository.findByUserId(id);
        if(iadlsList.size() > 0) {
            return iadlsList.get(0);
        } else {
            return new Iadls();
        }
    }
}