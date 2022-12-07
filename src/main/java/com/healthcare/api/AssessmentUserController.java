package com.healthcare.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.healthcare.api.common.HealthcareConstants;
import com.healthcare.model.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.model.entity.assessment.AssessmentUser;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.ActivityService;
import com.healthcare.service.AssessmentUserService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

/**
 * Activity controller
 */
@RestController
@RequestMapping("/api/assessment")
public class AssessmentUserController extends BaseController {

    @Autowired
    private AssessmentUserService assessmentUserService;

    @Autowired
    public AssessmentUserController(AssessmentUserService assessmentUserService) {
        this.assessmentUserService = assessmentUserService;
    }

    @ApiOperation(value = "Create assessment user", notes = "Create an assessment")
    @ApiParam(name = "assessmentUser", value = "assessment user to create", required = true)
    @PostMapping
    public ResponseEntity create(HttpServletRequest req, @RequestBody AssessmentUser assessmentUser) {
        Admin admin = (Admin) req.getAttribute(HealthcareConstants.AUTHENTICATED_ADMIN);
        assessmentUser.setAdmin(admin);
        return ResponseEntity.ok(assessmentUserService.save(assessmentUser).getId());
    }

    @ApiOperation(value = "get user's assessment by id", notes = "get user's assessment by id")
    @ApiImplicitParam(name = "id", value = "user's assessment id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public AssessmentUser read(@PathVariable Long id) {
        return assessmentUserService.findById(id);
    }

    @ApiOperation(value = "get user's review by assessment id", notes = "get user's review by assessment id")
    @ApiImplicitParam(name = "id", value = "user's assessment id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}/review")
    public ResponseEntity<AssessmentUser> readReview(@PathVariable Long id) {
        Review review = assessmentUserService.findReviewByAssessmentUserId(id);
        User user = assessmentUserService.findUserByAssessmentUserId(id);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setId(id);
        assessmentUser.setReview(review);
        assessmentUser.setUser(user);

        return new ResponseEntity<>(assessmentUser, HttpStatus.OK);
    }

    @ApiOperation(value = "Update assessment user", notes = "Update an assessment")
    @ApiParam(name = "assessmentUser", value = "assessment user to update", required = true)
    @PutMapping
    public ResponseEntity<AssessmentUser> update(@RequestBody AssessmentUser assessmentUser) {
        assessmentUser = assessmentUserService.save(assessmentUser);
        return new ResponseEntity<AssessmentUser>(assessmentUser, HttpStatus.OK);
    }

    @ApiOperation(value = "get all assessmentUser", notes = "get all assessmentUser")
    @GetMapping()
    public ResponseEntity<Page<AssessmentUser>> readAll(@RequestParam MultiValueMap<String, String> attributes) {
        MultiValueMapConverter<AssessmentUser> converter = new MultiValueMapConverter<>(attributes, AssessmentUser.class);
        return ResponseEntity.ok(assessmentUserService.findAll(converter.getData(), converter.getPageable()));
    }

    @ApiOperation(value = "get all assessmentUser", notes = "get all assessmentUser")
    @GetMapping("/getAllAssessmentByUser/{id}")
    public ResponseEntity<List<AssessmentUser>> readAll(@PathVariable Long id) {
        return ResponseEntity.ok(assessmentUserService.findByUserId(id));
    }
}