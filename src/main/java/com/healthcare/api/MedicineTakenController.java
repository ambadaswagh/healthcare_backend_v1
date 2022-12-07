package com.healthcare.api;

import com.healthcare.model.entity.assessment.AssessmentUser;
import com.healthcare.model.entity.assessment.MedicineTaken;
import com.healthcare.repository.AssessmentUserRepository;
import com.healthcare.repository.assessment.MedicineTakenRepository;
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
@RequestMapping(value = "/api/medicineTaken")
public class MedicineTakenController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MedicineTakenRepository medicineTakenRepository;

    @Autowired
    AssessmentUserRepository assessmentUserRepository;

    @ApiOperation(value = "save medicineTaken", notes = "save medicineTaken")
    @ApiParam(name = "medicineTaken", value = "medicineTaken to save", required = true)
    @PostMapping()
    public ResponseEntity<MedicineTaken> create(@RequestBody MedicineTaken medicineTaken) {
        medicineTaken = medicineTakenRepository.save(medicineTaken);
        saveAssessmentUser(medicineTaken);
        return new ResponseEntity<MedicineTaken>(medicineTaken, HttpStatus.OK);
    }

    private void saveAssessmentUser(@RequestBody MedicineTaken medicineTaken) {
        AssessmentUser assessmentUser = assessmentUserRepository.findOne(medicineTaken.getUserId());
        if(assessmentUser != null){
            assessmentUser.setMedicineTaken(medicineTaken);
            assessmentUserRepository.save(assessmentUser);
        }
    }
    @ApiOperation(value = "get medicineTaken by id", notes = "get medicineTaken by id")
    @ApiImplicitParam(name = "id", value = "medicineTaken id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public MedicineTaken read(@PathVariable("id") Long id) {
        logger.info("id : " + id);
        List<MedicineTaken> medicineTakenList = medicineTakenRepository.findByUserId(id);
        if(medicineTakenList.size() > 0) {
            return medicineTakenList.get(0);
        } else {
            return new MedicineTaken();
        }
    }
}
