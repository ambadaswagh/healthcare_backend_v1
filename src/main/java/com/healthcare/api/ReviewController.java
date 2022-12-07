package com.healthcare.api;

import static com.healthcare.api.common.HealthcareUtil.SET_DATE_RANGE_IF_NOT_PROVIDED;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.healthcare.model.entity.assessment.AssessmentUser;
import com.healthcare.repository.AssessmentUserRepository;
import com.healthcare.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.api.common.Validator;
import com.healthcare.dto.ActivityDTO;
import com.healthcare.dto.AssessmentReportResponseDTO;
import com.healthcare.dto.BasicReportFilterDTO;
import com.healthcare.dto.ServicePlanDTO;
import com.healthcare.model.entity.Activity;
import com.healthcare.model.entity.Review;
import com.healthcare.model.entity.ServicePlan;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.ActivityService;
import com.healthcare.service.ReviewService;
import com.healthcare.service.ServicePlanService;
import com.healthcare.service.VisitService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Review controller
 */
@RestController
@RequestMapping("/api/review")
public class ReviewController extends BaseController {

	private ReviewService reviewService;

	@Autowired
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	@Autowired
	private ServicePlanService servicePlanService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private VisitService visitService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AssessmentUserRepository assessmentUserRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

//	@ApiOperation(value = "Create review", notes = "Create review")
//	@ApiParam(name = "review", value = "review to create", required = true)
//	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
//	@PostMapping
//	public ResponseEntity create(@RequestBody Review review) {
//		return ResponseEntity.ok(reviewService.save(review).getId());
//	}

	@ApiOperation(value = "save review", notes = "save review")
	@ApiParam(name = "review", value = "review to save", required = true)
	@PostMapping()
	public ResponseEntity<Review> create(@RequestBody Review review) {
		review = reviewRepository.save(review);
		saveAssessmentUser(review);
		return new ResponseEntity<Review>(review, HttpStatus.OK);
	}

	private void saveAssessmentUser(@RequestBody Review review) {
		AssessmentUser assessmentUser = assessmentUserRepository.findOne(review.getAssessmentUserId());
		if(assessmentUser != null){
			assessmentUser.setReview(review);
			assessmentUserRepository.save(assessmentUser);
		}
	}

//	@ApiOperation(value = "Get review by Id", notes = "Get review info by reviewId")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "id", value = "review Id", required = true, dataType = "Long", paramType = "path"),
//			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header") })
//	@GetMapping("reviewId/{reviewId}")
//	public ResponseEntity get(@PathVariable("reviewId") String id) {
//		Long reviewId = parseId(id);
//		if (reviewId == null) {
//			return ResponseEntity.badRequest().build();
//		}
//
//		return ResponseEntity.ok(reviewService.findById(reviewId));
//	}

	@ApiOperation(value = "get review by id", notes = "get review by id")
	@ApiImplicitParam(name = "id", value = "review id", required = true, dataType = "Long" ,paramType = "path")
	@GetMapping("reviewId/{reviewId}")
	public Review read(@PathVariable("reviewId") Long id) {
		logger.info("id : " + id);
		List<Review> reviewList = reviewRepository.findByAssessmentUserId(id);
		if(reviewList.size() > 0) {
			return reviewList.get(0);
		} else {
			return new Review();
		}
	}

	@ApiOperation(value = "Update review", notes = "Update review")
	@ApiParam(name = "review", value = "review to update", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PutMapping
	public void save(@RequestBody Review review) {
		reviewService.save(review);
	}

	@ApiOperation(value = "Delete review", notes = "Delete review")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "review Id", required = true, dataType = "Long", paramType = "path"),
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header") })

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") String id, HttpServletResponse response) {
		Long reviewId = parseId(id);

		if (reviewId == null) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return;
		}

		reviewService.deleteById(reviewId);
	}

	@ApiOperation(value = "get reviews by user id", notes = "get reviews by user id")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "user id", required = true, dataType = "Long", paramType = "path"),
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header") })

	@RequestMapping(value = "/reviewbyuser/{userId}", method = RequestMethod.GET)
	public List<Review> getReviewByUser(@PathVariable("userId") Long userId) {
		return reviewService.findByUser(userId);
	}

	@ApiOperation(value = "get assessment report", notes = "get assessment report")
	@ApiParam(name = "basicReportFilterDTO", value = "basicReportFilterDTO", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@RequestMapping(value = "/assessment_report", method = RequestMethod.POST)
	public AssessmentReportResponseDTO getAssessmentReport(HttpServletRequest req,
														   @RequestBody BasicReportFilterDTO basicReportFilterDTO) {

		Validator.validateCompanyAndAgency(basicReportFilterDTO);

		Validator.AUTH_ADMIN(req, basicReportFilterDTO);

		SET_DATE_RANGE_IF_NOT_PROVIDED(basicReportFilterDTO);

		List<Review> reviews = reviewService.findByUser(basicReportFilterDTO.getUserId());
		List<Activity> activities = activityService.getActivityByUser(basicReportFilterDTO);
		List<ServicePlan> servicePlans = servicePlanService.getServicePlanByUser(basicReportFilterDTO);

		List<ActivityDTO> activityDTOList = new ArrayList<ActivityDTO>();

		for (Activity activity : activities) {
			activityDTOList.add(new ActivityDTO(activity));
		}

		List<ServicePlanDTO> servicePlanDTOS = new ArrayList<ServicePlanDTO>();

		for (ServicePlan servicePlan : servicePlans) {
			servicePlanDTOS.add(new ServicePlanDTO(servicePlan));
		}

		AssessmentReportResponseDTO responseDTO = new AssessmentReportResponseDTO();
		responseDTO.setActivityDTOS(activityDTOList);
		responseDTO.setReviews(reviews);
		responseDTO.setServicePlanDTOS(servicePlanDTOS);

		return responseDTO;
	}

	@ApiOperation(value = "Get all reviews", notes = "Get all reviews")
	@GetMapping()
	public ResponseEntity<Page<Review>> readAll(@RequestParam MultiValueMap<String, String> attributes) {
		MultiValueMapConverter<Review> converter = new MultiValueMapConverter<>(attributes, Review.class);
		return ResponseEntity.ok(reviewService.findAll(converter.getData(), converter.getPageable()));
	}

}