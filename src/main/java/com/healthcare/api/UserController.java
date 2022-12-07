package com.healthcare.api;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareConstants.NOT_AUTHORIZED;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.healthcare.api.common.HealthcareConstants;
import com.healthcare.container.checkMealAllergyRequest;
import com.healthcare.service.*;

import com.healthcare.dto.*;

import org.apache.commons.lang.time.DateUtils;
import com.healthcare.api.common.HealthcareUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.api.auth.UtilsResponse;
import com.healthcare.exception.UserException;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Ride;
import com.healthcare.model.entity.RideLineDaily;
import com.healthcare.model.entity.User;
import com.healthcare.model.entity.assessment.HealthStatus;
import com.healthcare.model.entity.assessment.Housing;
import com.healthcare.model.entity.assessment.Nutrition;

import com.healthcare.model.entity.*;

import com.healthcare.model.enums.StatusEnum;
import com.healthcare.model.response.Response;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.repository.*;
import com.healthcare.service.impl.CustomUserValidator;
import com.healthcare.util.PasswordUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.sql.Timestamp;


@RestController
@RequestMapping(value = "/api/user")
public class UserController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private MedicationRepository medicationRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	CustomUserValidator customUserValidator;
	@Autowired
	private UtilsResponse responseBuilder;

	@Autowired
	RideRepository rideRepository;

	@Autowired
	RideLineHasDriverRepository rideLineHasDriverRepository;

	@Autowired
	VisitRepository visitRepository;

	@Autowired
	SeatRepository seatRepository;

	@Autowired
  	MealService mealService;

	@Autowired
	TripAnalyzeService tripAnalyzeService;

	@Autowired
	RideLineDailyRepository rideLineDailyRepository;

	@Autowired
	RideLineRepository rideLineRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	InsuranceHistoryRepository insuranceHistoryRepository;

	@Autowired
  	AgencyService agencyService;

	@Autowired
	AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

	@ApiOperation(value = "save user", notes = "save a new user")
	@ApiParam(name = "user", value = "user to update", required = true)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<User> create(HttpServletRequest req,@RequestBody User user) {
		customUserValidator.checkUserNameExists(user.getUsername());

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		String pin  = getPIN(user);
		user.setPin(pin);
		user.setBoxNumber(pin);

		String barcode = PasswordUtils.hashPassword(pin);
		user.setQrCode(barcode);
		String hashedPassword = PasswordUtils.hashPassword(user.getPassword());
		user.setPassword(hashedPassword);

		if(user.getAuthorizationEnd()==null || user.getAuthorizationStart()==null)
		{
			user.setStatus(StatusEnum.INACTIVE);
		}
		user.setMedication(medicationRepository.save(user.getMedication()));
		if(user.getProfilePhoto() != null) {
			user.setProfilePhoto(documentRepository.save(user.getProfilePhoto()));
		}
		user = userRepository.save(user);

		createVisitAndRide(user);

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@ApiOperation(value = "check pin for agency", notes = "check pin for agency")
	@ApiParam(name = "agencyId", value = "agency id", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PostMapping("/pin/validate/{agencyId}/{pin}")
	public ResponseEntity<Boolean> checkPinValidity(@PathVariable long agencyId, @PathVariable String pin) {
		boolean isValid = userService.validPin(pin, agencyId);
		return new ResponseEntity<Boolean>(isValid, HttpStatus.OK);
	}


	@ApiOperation(value = "generate pin", notes = "generate pin")
	@ApiParam(name = "agency id", value = "generate pin", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@GetMapping("/pin/generate/{agencyId}")
	public ResponseEntity<String> generatePin(@PathVariable long agencyId) {
		String generatedPin = userService.generatePin(agencyId);
		return new ResponseEntity<String>(generatedPin, HttpStatus.OK);
	}

	private String getPIN(User user) {
		String generatedPin = null;

		if(user.isGeneratePin() || StringUtils.isEmpty(user.getPin())) {
			generatedPin = userService.generatePin(user.getAgency().getId());
		}else{

			if(StringUtils.isEmpty(user.getPin()) || user.getPin().length() > 4){
				throw new UserException("PIN should be of 4 digit or less.");
			}

			try {
				Integer.parseInt(user.getPin());
			} catch (NumberFormatException e){
				throw new UserException("PIN should be a Number.");
			}

			boolean pinUsed = userService.isPinUsed(user.getPin(),user.getAgency().getId(), user.getId());

			if(pinUsed){
				throw new UserException("PIN already taken! Please provide another PIN or use Auto generate option ");
			}else{
				generatedPin = user.getPin();
			}
		}

		return generatedPin;
	}

	@ApiOperation(value = "get user by Id", notes = "get user info by userId")
	@ApiImplicitParam(name = "id", value = "user Id", required = true, dataType = "Long" ,paramType = "path")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public User read(@PathVariable Long id) {
		return userRepository.findUserById(id);
	}

	@ApiOperation(value = "get user trip driver stats", notes = "get user trip driver stats info by userId")
	@ApiImplicitParam(name = "id", value = "user Id", required = true, dataType = "Long" ,paramType = "path")
	@RequestMapping(value = "/{id}/driverFrequency", method = RequestMethod.GET)
	public List<Map<String, String>> readDriverFrequency(@PathVariable Long id) {
		return tripAnalyzeService.findDriverFrequency(id);
	}

	@ApiOperation(value = "get qrCode for user", notes = "get qrCode for user")
	@ApiImplicitParams ({
			@ApiImplicitParam(name = "id", value = "user Id", required = true, dataType = "Long", paramType = "path")
			,@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	})
	@RequestMapping(value = "/qrCode/{id}", method = RequestMethod.GET)
	public String generateBarcode(@PathVariable Long id) {
		User user = read(id);

		if(user == null){
			throw new UserException("User not found.");
		}

		if(user.getQrCode() == null ){
			throw new UserException("QR Code not found for this user, Please update PIN first to get QR Code");
		}

		return user.getQrCode();
	}


	@ApiOperation(value = "get all Pin", notes = "get all Pin")
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@RequestMapping(value = "/allpin", method = RequestMethod.GET)
	public List<String>  getAllPIN(HttpServletRequest req) {
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		if(admin == null) return new ArrayList<String>();
		return userService.findAllPIN(admin.getId());
	}

	@ApiOperation(value = "update user's note", notes = "update a user's note")
	@ApiParam(name = "user", value = "user to update", required = true)
	@RequestMapping(value = "/note",method = RequestMethod.PUT)
	public ResponseEntity<User> update_user_note(HttpServletRequest req,@RequestBody User user) {
		//System.err.println(user.getNote());
		userService.update_note(user.getId(), user.getNote());
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}


	@ApiOperation(value = "update user", notes = "update a user")
	@ApiParam(name = "user", value = "user to update", required = true)
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<User> update(HttpServletRequest req,@RequestBody User user) {

		String pin  = getPIN(user);
		user.setPin(pin);

		boolean isBoxUsed = userService.isBoxNumberUsed(user.getBoxNumber(),user.getAgency().getId(), user.getId());

		if(isBoxUsed){
			throw new UserException("Box already taken! Please provide another Box Number");
		}

		// This is to prevent update password from update screen.
		User dbUser = userRepository.findUserById(user.getId());
		user.setPassword(dbUser.getPassword());


		String barcode = PasswordUtils.hashPassword(pin);
		user.setQrCode(barcode);

		if(user.getAuthorizationEnd()==null || user.getAuthorizationStart()==null)
		{
			user.setStatus(StatusEnum.INACTIVE);
		}
		user = userService.save(user);

		createVisitAndRide(user);

		createInsuranceHistory(user);

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	void createVisitAndRide(User user) {
		if(user.getId() != null) {
			rideRepository.deleteByUser(user);
			visitRepository.deleteVisitByUser(user);
		}
		if(user.getAuthorizationStart() != null
				&& user.getAuthorizationEnd() != null
				&& !StringUtils.isEmpty(user.getDaysInWeek())) {
			List<String> daysList = Arrays.asList(user.getDaysInWeek().split(","));
//			Collections.replaceAll(daysList, "7", "0");
//			Collections.replaceAll(daysList, "1", "7");
//			Collections.replaceAll(daysList, "0", "1");

			List<Ride> rides = new ArrayList<Ride>();
			List<Visit> visits = new ArrayList<>();
			LocalDate startDate = user.getAuthorizationStart().toLocalDateTime().toLocalDate();;
			LocalDate endDate = user.getAuthorizationEnd().toLocalDateTime().toLocalDate();;
			int needTrip = user.getNeedTrip().intValue();
			if(user.getRideLine() != null)
				user.setRideLine(rideLineRepository.findOne(user.getRideLine().getId()));
			for(LocalDate date = startDate; date.isBefore(endDate) || date.equals(endDate); date = date.plusDays(1)) {
				LocalDate vacationStartDate = null;
				LocalDate vacationEndDate = null;
				boolean vacationExits = false;
				if(user.getVacationStart() != null
						&& user.getVacationEnd() != null) {
					vacationExits = true;
					vacationStartDate = user.getVacationStart().toLocalDateTime().toLocalDate();
					vacationEndDate = user.getVacationEnd().toLocalDateTime().toLocalDate();
				}

				if(!vacationExits
						|| date.isBefore(vacationStartDate)
						|| date.isAfter(vacationEndDate)) {
					int day = date.getDayOfWeek().getValue();
					if(daysList.contains(String.valueOf(day))) {
						if(needTrip == 1 || needTrip == 3) {

							RideLineDaily rideLineDaily = getRideLineDaily(user, date);

							Ride ride = new Ride();
							ride.setPickup(user.getAddressOne() + " " + user.getAddressTwo() + " " + user.getCity() + " " + user.getState() + " " + user.getZipcode());
							ride.setDropoff(user.getAgency().getAddressOne() + " " + user.getAgency().getAddressTwo() + " " + user.getAgency().getCity() + " " + user.getAgency().getState() + " " + user.getAgency().getZipcode());
							ride.setDate(getDate(date));
							ride.setPhone(user.getPhone());
							ride.setEmail(user.getEmail());
							ride.setNeedTrip(new Long(1));
							ride.setUser(user);
							ride.setRideLine(user.getRideLine());
							ride.setStatus(new Long(1));
							ride.setRideLineDaily(rideLineDaily);
							ride.setRideColor(user.getRideLine().getRideLineColor());
							rides.add(ride);
						}
						if(needTrip == 2 || needTrip == 3) {

							RideLineDaily rideLineDaily = getRideLineDaily(user, date);

							Ride ride = new Ride();
							ride.setDropoff(user.getAddressOne() + " " + user.getAddressTwo() + " " + user.getCity() + " " + user.getState() + " " + user.getZipcode());
							ride.setPickup(user.getAgency().getAddressOne() + " " + user.getAgency().getAddressTwo() + " " + user.getAgency().getCity() + " " + user.getAgency().getState() + " " + user.getAgency().getZipcode());
							ride.setDate(getDate(date));
							ride.setPhone(user.getPhone());
							ride.setEmail(user.getEmail());
							ride.setNeedTrip(new Long(2));
							ride.setUser(user);
							ride.setRideLine(user.getRideLine());
							ride.setStatus(new Long(1));
							ride.setRideLineDaily(rideLineDaily);
							ride.setRideColor(user.getRideLine().getRideLineColor());
							rides.add(ride);
						}

						Visit visit = new Visit();
						visit.setUser(user);
						visit.setCheckInTime(new Timestamp(getDate(date).getTime()));
						visit.setPin(user.getPin());
						visit.setAgency(user.getAgency());
						visit.setReservedDate(getDate(date));
						visit.setStatus("RESERVED");
						visit.setActive(0);
						visits.add(visit);
					}
				}
			}
			if(rides.size() > 0)
				rideRepository.save(rides);
			visitRepository.save(visits);
		}
	}

	private Date getDate(LocalDate date) {
		return DateUtils.addHours(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()), 12);
	}

	void createInsuranceHistory(User user) {

		Integer i = 1;
		if(user.getInsurance()!= null){
			i = insuranceHistoryRepository.getInsuranceHistory(user.getId(), user.getInsurance().getId(), user.getAuthorizationStart(), user.getAuthorizationEnd(), user.getAuthorizationCode(), user.getInsurance().getCode());
		}

		if(i == null){
			InsuranceHistory newInsuranceHistory = new InsuranceHistory();
			newInsuranceHistory.setUser(user);
			newInsuranceHistory.setInsurance(user.getInsurance());
			newInsuranceHistory.setAuthorizationStart(user.getAuthorizationStart());
			newInsuranceHistory.setAuthorizationEnd(user.getAuthorizationEnd());
			newInsuranceHistory.setAuthorizationCode(user.getAuthorizationCode());
			newInsuranceHistory.setInsuranceCode(user.getInsurance().getCode());

			insuranceHistoryRepository.save(newInsuranceHistory);

		}
	}

	private RideLineDaily getRideLineDaily(User user, LocalDate date) {
		RideLineDaily rideLineDaily = null;
		List<RideLineDaily> rideLineDailies = rideLineDailyRepository.findByDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()), user.getRideLine().getId());
		if(rideLineDailies.size() == 0) {
			rideLineDaily = new RideLineDaily();
			rideLineDaily.setDate(getDate(date));
			rideLineDaily.setRideLine(user.getRideLine());
			rideLineDaily.setName(user.getRideLine().getName());
			rideLineDaily.setBorough(user.getRideLine().getBorough());
			rideLineDaily.setNote(user.getRideLine().getNote());
			rideLineDaily.setDriver(user.getRideLine().getDriver());
			rideLineDaily.setVehicle(user.getRideLine().getVehicle());
			rideLineDaily.setStatus(new Long(1));
			rideLineDaily.setRideLineDailyColor(user.getRideLine().getRideLineColor());

			int day = date.getDayOfWeek().getValue();
			List<RideLineHasDriver> rideLineHasDrivers = rideLineHasDriverRepository.getRideLineHasDriverForRideLineOnDay(user.getRideLine().getId(), new Long(day));
			if(rideLineHasDrivers.size() > 0) {
				for(RideLineHasDriver rideLineHasDriver : rideLineHasDrivers) {
					if(rideLineHasDriver.getNeedTrip().intValue() == 1) {
						rideLineDaily.setInboundDriver(rideLineHasDriver.getDriver());
					} else if(rideLineHasDriver.getNeedTrip().intValue() == 2) {
						rideLineDaily.setOutboundDriver(rideLineHasDriver.getDriver());
					}
				}
			}

			rideLineDaily = rideLineDailyRepository.save(rideLineDaily);
		} else {
			rideLineDaily = rideLineDailies.get(0);
		}
		return rideLineDaily;
	}

	@ApiOperation(value = "delete user", notes = "delete a user")
	@ApiImplicitParam(name = "id", value = "user Id", required = true, dataType = "Long" ,paramType = "path")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Long id) {
		userService.deleteById(id);
	}

	@ApiOperation(value = "get all user", notes = "get all user")
	@GetMapping
	public ResponseEntity<Page<User>> readAll(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {
		/*MultiValueMapConverter<User> converter = new MultiValueMapConverter<>(attributes, User.class);
		return userService.findAll(converter.getData(), converter.getPageable());*/

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		MultiValueMapConverter<User> converter = new MultiValueMapConverter<>(attributes, User.class);

		if(isSuperAdmin(admin)){
			return ResponseEntity.ok(userService.findAll(converter.getPageable()));
		} else if (isCompanyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				return ResponseEntity.ok(userService.findByCompanyPage(adminAgencyCompanyOrganization.getCompany().getId(), converter.getPageable()));
			}

		} else if (isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				return ResponseEntity.ok(
						userService.findByCompanyAndAgency(adminAgencyCompanyOrganization.getCompany().getId(),adminAgencyCompanyOrganization.getAgency().getId(), converter.getPageable()));
			}
		}

		return ResponseEntity.ok(new PageImpl<User>(new ArrayList<User>()));
	}

	@ApiOperation(value = "get all user", notes = "get all user")
	@GetMapping("/valid")
	public List<User> readAllWithRoles(HttpServletRequest req) {
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		if(isSuperAdmin(admin)){
			return userService.findAll();
		} else if (isCompanyAdmin(admin)){
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
			if (adminAgencyCompanyOrganization != null) {
				return userService.findByCompanyId(adminAgencyCompanyOrganization.getCompany().getId());
			}
		} else if (isAgencyAdmin(admin)){
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				return userService.findByAgencyId(adminAgencyCompanyOrganization.getAgency().getId());
			}
		}

		return null;
	}

	@ApiOperation(value = "disable user", notes = "disable a user")
	@ApiImplicitParam(name = "id", value = "user Id", required = true, dataType = "Long" ,paramType = "path")
	@RequestMapping(value = "/{id}/disable", method = RequestMethod.PUT)
	public void disable(@PathVariable Long id) {
		userService.disableById(id);
	}

	@ApiOperation(value = "search user", notes = "search a user")
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@GetMapping("/user_search")
	public List<User> searchUser(@RequestParam("search") String search){
		return userService.searchUser(search);
	}

	@ApiOperation(value = "search user", notes = "search a user")
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@GetMapping("/user_search_admin")
	public List<User> searchUserAdmin(HttpServletRequest req, @RequestParam("search") String search){
		Admin admin = (Admin) req.getAttribute(HealthcareConstants.AUTHENTICATED_ADMIN);
		return userService.searchUserForAdmin(search, adminAgencyCompanyOrganizationService.findByAdminId(admin.getId()));
	}

	@ApiOperation(value = "search user", notes = "search a user")
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@GetMapping("/userSearchForRide")
	public List<User> searchUserForRide(@RequestParam(value = "agencyId") Long agencyId,
								 	    @RequestParam(value = "companyId") Long companyId){
		return userService.searchUserForRide(agencyId, companyId);
	}

	@ApiOperation(value = "search user", notes = "search a user")
	@ApiParam(name = "userSearchDTO", value = "user search dto", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PostMapping("/user_search_screen")
	public List<User> searchUserScree(@RequestBody UserSearchDTO userSearchDTO){
		return userService.searchUser(userSearchDTO);
	}


	@GetMapping("/findAllSeniorsForRideLine/{id}")
	public List<User> findAllSeniorsForRideLine(@PathVariable Long id){
		return userService.findAllSeniorsForRideLine(id);
	}
	@GetMapping("/findAllSeniorsWithoutRideLine")
	public List<User> findAllSeniorsWithoutRideLine(@RequestParam(value = "agencyId") Long agencyId,
													@RequestParam(value = "companyId") Long companyId){
		return userService.findAllSeniorsWithoutRideLine(agencyId, companyId);
	}

	@ApiOperation(value = "List user have authorization end next few days", notes = "Default will be 10 days")
	@ApiImplicitParam(name = "days", value = "how many next day. Default value is 10", dataType = "Integer" ,paramType = "path")
	@RequestMapping(value = "/authorization-expire/{days}", method = RequestMethod.GET)
	public ResponseEntity<?> getAuthorizationEndFewDays(HttpServletRequest req, @PathVariable Integer days, @RequestParam MultiValueMap<String, String> attributes) {

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		if (HealthcareUtil.isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		Integer realDay = days == null ? 0 : days;
		if(realDay < 0){
			return ResponseEntity.badRequest().body("days must be larger than 0");
		}

		MultiValueMapConverter<User> converter = new MultiValueMapConverter<>(attributes, User.class);
		return ResponseEntity.ok(userService.getUserAuthorizationEndNextDays(realDay, admin, converter.getPageable()));
	}


	@ApiOperation(value = "List user have assessment in few days", notes = "Default will be 10 days")
	@ApiImplicitParam(name = "days", value = "how many next day. Default value is 10", dataType = "Integer" ,paramType = "path")
	@RequestMapping(value = "/assessment-reminder/{days}", method = RequestMethod.GET)
	public ResponseEntity<?> getAssessmentReminderFewDays(HttpServletRequest req, @PathVariable Integer days, @RequestParam MultiValueMap<String, String> attributes) {

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		if (HealthcareUtil.isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		Integer realDay = days == null ? 0 : days;
		if(realDay < 0){
			return ResponseEntity.badRequest().body("days must be larger than 0");
		}

		MultiValueMapConverter<User> converter = new MultiValueMapConverter<>(attributes, User.class);
		return ResponseEntity.ok(userService.getAssessmentReminder(realDay, admin, converter.getPageable()));
	}

	@ApiOperation(value = "List user have assessment end next few days", notes = "Default will be 10 days")
	@ApiImplicitParam(name = "days", value = "how many next day. Default value is 10", dataType = "Integer" ,paramType = "path")
	@RequestMapping(value = "/assessment-expire/{days}", method = RequestMethod.GET)
	public ResponseEntity<?> getAssessmentEndFewDays(@PathVariable Integer days, @RequestParam MultiValueMap<String, String> attributes) {
		Integer realDay = days == null ? 0 : days;
		if(realDay < 0){
			return ResponseEntity.badRequest().body("days must be larger than 0");
		}

		MultiValueMapConverter<User> converter = new MultiValueMapConverter<>(attributes, User.class);
		return ResponseEntity.ok(userService.getUserAssessmentEndNextDays(realDay, converter.getPageable()));
	}

	@ApiOperation(value = "Get user birthday by date", notes = "Get user birthday by date")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "date", value = "Date for birthday format yyyy-MM-dd", required = true, dataType = "String" ,paramType = "path")
	})
	@GetMapping("/birthday/{date}")
	public Response getBirthdayByDate(@PathVariable("date") String date) {
		List<User> users = userService.getUserHaveBirthday( date);
		return responseBuilder.successResponse(users, "OK");
	}

	@ApiOperation(value = "Get user birthday by date", notes = "Get user birthday by date")
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@GetMapping("/birthday")
	public Response getBirthdayToday() {
		List<User> users = userService.getUserHaveBirthday("");
		return responseBuilder.successResponse(users, "OK");
	}

	@ApiOperation(value = "List user have birthday next few days", notes = "Default will be 7 days")
	@ApiImplicitParam(name = "days", value = "how many next day. Default value is 7", dataType = "Integer" ,paramType = "path")
	@RequestMapping(value = "/birthday/feature/{days}", method = RequestMethod.GET)
	public ResponseEntity<?> getAuthorizationBirthdayFeature(HttpServletRequest req, @PathVariable Integer days, @RequestParam MultiValueMap<String, String> attributes) {

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}


		Integer realDay = days == null ? 0 : days;
		if(realDay < 0){
			return ResponseEntity.badRequest().body("days must be larger than 0");
		}

		MultiValueMapConverter<User> converter = new MultiValueMapConverter<>(attributes, User.class);
		return ResponseEntity.ok(userService.getAuthorizationBornFromDateToDate(realDay, admin, converter.getPageable()));
	}

	@ApiOperation(value = "save housing_status", notes = "save housing_status")
	@ApiParam(name = "housing", value = "housing to save", required = true)
	@PostMapping(value="/saveHousing")
	public ResponseEntity<HousingDTO> create(HttpServletRequest req,@RequestBody HousingDTO housingDto) {
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		System.out.println("inside controller for housing");
		DataBinder binder = new DataBinder(housingDto);
		/*binder.setValidator(saveDriverValidator);
		binder.validate();*/
		BindingResult result = binder.getBindingResult();
		if (result.hasErrors()) {
			return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		Housing housing = new Housing();
		housing.setComment(housingDto.getComment());
		housing.setHomeSafetyChecklist(housingDto.getHomeSafetyChecklist());
		housing.setIsNeighborhoddSafetyAnIssue(housingDto.getIsNeighborhoddSafetyAnIssue());
		housing.setTypeOfHousing(housingDto.getTypeOfHousing());
		housing.setUserId(admin.getId());
		housing.setOtherHomeSafety(housingDto.getOtherHomeSafety());
		housing.setOtherHousing(housingDto.getOtherHousing());
		housing = userService.save(housing);
		return new ResponseEntity<HousingDTO>(housingDto, HttpStatus.OK);
	}

	@ApiOperation(value = "save health_status", notes = "save health_status")
	@ApiParam(name = "healthstatus", value = "healthstatus to save", required = true)
	@PostMapping(value="/saveHealthStatus")
	public ResponseEntity<HealthStatusDTO> create(HttpServletRequest req,@RequestBody HealthStatusDTO healthStatusdto) {
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		DataBinder binder = new DataBinder(healthStatusdto);
		/*binder.setValidator(saveDriverValidator);
		binder.validate();*/
		BindingResult result = binder.getBindingResult();
		if (result.hasErrors()) {
			return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		HealthStatus healthStatus = new HealthStatus();
		healthStatus.setClinicHmo(healthStatusdto.getClinicHmo());
		healthStatus.setHaveAssistiveDevice(healthStatusdto.getHaveAssistiveDevice());
		healthStatus.setHospital(healthStatusdto.getHospital());
		healthStatus.setHospitalizedOrEmergencyRoomLastSixMonths(healthStatusdto.getHospitalizedOrEmergencyRoomLastSixMonths());
		healthStatus.setOther(healthStatusdto.getOther());
		healthStatus.setParticipantIllnessAndDisability(healthStatusdto.getParticipantIllnessAndDisability());
		healthStatus.setPrimaryPhysician(healthStatusdto.getPrimaryPhysician());
		healthStatus.setUnderstandOthers(healthStatusdto.getUnderstandOthers());
		healthStatus.setDateOfLastPCP(healthStatusdto.getDateOfLastPCP());
		healthStatus.setUserId(admin.getId());
		healthStatus = userService.save(healthStatus);
		return new ResponseEntity<HealthStatusDTO>(healthStatusdto, HttpStatus.OK);
	}

	@ApiOperation(value = "get all healthstatus", notes = "get all healthstatus")
	@GetMapping("/getHealthStatus")
	public HealthStatus readAllHealthStatus() {
		HealthStatus healthStatus = userService.findAllHealthStatus();
		return healthStatus;
	}

	@ApiOperation(value = "get all housing", notes = "get all housing")
	@GetMapping("/getHousingStatus")
	public Housing readAllHousing() {
		Housing housing = userService.findAllHousing();
		return housing;
	}

	@ApiOperation(value = "get all user by company id", notes = "get all user by company id")
	@GetMapping("/company/{companyId}")
	public ResponseEntity<Page<User>> findAllUserByCompany(@RequestParam MultiValueMap<String, String> attributes, @PathVariable("companyId") Long companyId) {
		MultiValueMapConverter<User> converter = new MultiValueMapConverter<>(attributes, User.class);

		return ResponseEntity.ok(userService.findByCompanyPage(companyId, converter.getPageable()));
	}

	@ApiOperation(value = "get all user by company id and agency id", notes = "get all user by company id and agency id")
	@GetMapping("/company/{companyId}/agency/{agencyId}")
	public ResponseEntity<Page<User>> findAllUserByCompanyAndAgency(@RequestParam MultiValueMap<String, String> attributes,
																	@PathVariable("companyId") Long companyId,
																	@PathVariable("agencyId") Long agencyId) {
		MultiValueMapConverter<User> converter = new MultiValueMapConverter<>(attributes, User.class);

		return ResponseEntity.ok(userService.findByCompanyAndAgency(companyId, agencyId, converter.getPageable()));
	}

	@ApiOperation(value = "List user have vacation next few days", notes = "Default will be 7 days")
	@ApiImplicitParam(name = "days", value = "how many next day. Default value is 7", dataType = "Integer" ,paramType = "path")
	@RequestMapping(value = "/vacation/feature/{days}", method = RequestMethod.GET)
	public ResponseEntity<?> getVacationSeniorExpiring(HttpServletRequest req, @PathVariable Integer days, @RequestParam MultiValueMap<String, String> attributes) {

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}


		Integer realDay = days == null ? 0 : days;
		if(realDay < 0){
			return ResponseEntity.badRequest().body("days must be larger than 0");
		}

		MultiValueMapConverter<User> converter = new MultiValueMapConverter<>(attributes, User.class);
		return ResponseEntity.ok(userService.getVacationSeniorReminder(realDay, admin, converter.getPageable()));
	}

  @ApiOperation(value = "check user has allergy in the meal", notes = "check user has allergy in the meal")
  @PostMapping(value = "/checkMealAllergy/{id}")
  public Map<String, List<String>> checkMealAllergy(@PathVariable("id") Long id, @RequestBody checkMealAllergyRequest request) {
    Map<String, List<String>> response = new HashMap<>();

    Agency agency = agencyService.findById(id);
    User user = getUser(request.pin, agency);

    List<FoodAllergy> userFoodAllergies = user.getFoodAllergies();
    for (Meal meal : request.meals) {
      meal = mealService.findById(meal.getId());
      List<String> allergyIngredients = new ArrayList<>();
      for (Ingredient ingredient : meal.getIngredientList()){
        for (FoodAllergy foodAllergy : ingredient.getFoodAllergies()) {
          if(userFoodAllergies.contains(foodAllergy) ) {
            allergyIngredients.add(ingredient.getName());
            break;
          }
        }
      }
      if(!allergyIngredients.isEmpty()){
        response.put(meal.getName(), allergyIngredients);
      }
    }
    return  response;
  }

  private User getUser(String pin, Agency agency) {
    if (agency == null || agency.getId() == null) {
      throw new UserException("Agency can't empty!");
    }

    User user = userService.getUserByPIN(pin, agency.getId());

    if (user == null) {
      throw new UserException("User is not existed!");
    }

    return user;
  }

	@ApiOperation(value = "Add black list senior", notes = "Add black list senior")
	@ApiParam(name = "user", value = "dd black list senior", required = true)
	@RequestMapping(value="/add/blacklist/{userId}", method = RequestMethod.PUT)
	public ResponseEntity<User> addBlacklistSenior(HttpServletRequest req, @PathVariable("userId") Long userId, @RequestBody BlackListDTO dto) {

		User user = userRepository.findUserById(userId);

		if(user == null){
			throw new UserException("User is not existed!");
		}

		if (dto == null || dto.getId() == null) {
			throw new UserException("Senior is not existed");
		}

		// This is to prevent update password from update screen
		List<BlackListDTO> blacklists = user.getBlacklistUser();

		boolean isDriver = false;
		if ("Driver".equals(dto.getType())) {
			blacklists = user.getBlacklistDriver();
			isDriver = true;
		}

		if (blacklists == null) {
			blacklists = new ArrayList<BlackListDTO>();
		}

		boolean isUpdated = false;
		for (BlackListDTO blackListDTO: blacklists) {
			if (blackListDTO.getId() == dto.getId()) {
				isUpdated = true;
				blackListDTO.setNote(dto.getNote());
				break;
			}
		}

		if (!isUpdated) {
			// add black list dto to user
			blacklists.add(dto);
		}

		if (!isDriver) {
			user.setBlacklistUser(blacklists);
		} else {
			user.setBlacklistDriver(blacklists);
		}

		userService.save(user);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}


	@ApiOperation(value = "Add black list senior", notes = "Add black list senior")
	@ApiParam(name = "user", value = "dd black list senior", required = true)
	@RequestMapping(value="/delete/blacklist/{userId}", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteBlacklistSenior(HttpServletRequest req, @PathVariable("userId") Long userId, @RequestBody BlackListDTO dto) {

		User user = userRepository.findUserById(userId);

		if(user == null){
			throw new UserException("User is not existed!");
		}

		// This is to prevent update password from update screen
		List<BlackListDTO> blacklists = user.getBlacklistUser();

		boolean isDriver = false;
		if ("Driver".equals(dto.getType())) {
			blacklists = user.getBlacklistDriver();
			isDriver = true;
		}

		for (int i = 0; i < blacklists.size(); i++) {
			if (dto.getId() == blacklists.get(i).getId()) {
				blacklists.remove(i);
				break;
			}
		}

		if (!isDriver) {
			user.setBlacklistUser(blacklists);
		} else {
			user.setBlacklistDriver(blacklists);
		}

		userService.save(user);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@ApiOperation(value = "get all user", notes = "get all user")
	@GetMapping(value="/typeahead")
	public List<User> findAllForTypeahead(HttpServletRequest req, @RequestParam("search") String search) {

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		if(isSuperAdmin(admin)){
			return userService.searchUserByFirstName(search);
		} else if (isCompanyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				return userService.searchUserByFirstNameByCompany(adminAgencyCompanyOrganization.getCompany().getId(), search);
			}

		} else if (isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				return userService.searchUserByFirstNameByCompanyAgency(adminAgencyCompanyOrganization.getCompany().getId(), adminAgencyCompanyOrganization.getAgency().getId(), search);
			}
		}

		return new ArrayList<User>();
	}
}
