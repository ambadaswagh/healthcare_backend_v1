package com.healthcare.api;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareConstants.NOT_AUTHORIZED;
import static com.healthcare.api.common.HealthcareUtil.SET_DATE_RANGE_IF_NOT_PROVIDED;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.HealthcareUtil.isNull;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.healthcare.api.common.HealthcareUtil;
import com.healthcare.container.VisitUpdateRequest;
import com.healthcare.container.VisitUpdateResponse;
import com.healthcare.model.entity.*;
import com.healthcare.service.*;
import com.healthcare.util.DateUtils;
import net.minidev.json.JSONObject;
import org.joda.time.DateTime;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.api.common.Validator;
import com.healthcare.api.model.ExpressVisitRequestDTO;
import com.healthcare.api.model.MobileCheckinRequestDTO;
import com.healthcare.api.model.VisitReportRequest;
import com.healthcare.api.model.VisitReportResponse;
import com.healthcare.api.model.VisitRequest;
import com.healthcare.api.model.VisitSummary;
import com.healthcare.dto.BasicReportFilterDTO;
import com.healthcare.dto.BillingReportResponseDTO;
import com.healthcare.dto.ReportDTO;
import com.healthcare.dto.VisitDTO;
import com.healthcare.dto.VisitRequestDTO;
import com.healthcare.dto.VisitSeniorStatsReqDTO;
import com.healthcare.dto.VisitSeniorStatsResDTO;
import com.healthcare.dto.VisitStatusDTO;
import com.healthcare.exception.UserException;
import com.healthcare.model.enums.SeatStatusEnum;
import com.healthcare.model.enums.VisitStatusEnum;
import com.healthcare.pagination.MultiValueMapConverter;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@RestController
@RequestMapping(value = "/api/visit")
public class VisitController {

    @Autowired
    private VisitService visitService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private TableService tableService;

    @Autowired
    private HealthInsuranceClaimService healthInsuranceClaimService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private MealService mealService;

    @Autowired
    private ActivityService activityService;

    @Autowired
	private AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

    @ApiOperation(value = "save visit", notes = "save a new visit")
    @ApiParam(name = "visit", value = "visit to update", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Visit> create(HttpServletRequest req, @RequestBody Visit request) {
        Visit visit = request;
        String pin = getDigitPin(visit.getPin());
        if (pin == null) {
            return new ResponseEntity<Visit>(visit, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Visit>(visitService.createVisit(visit), HttpStatus.OK);
    }

    private Seat getSeat(Visit visit) {
        if (visit.getSeat() == null || visit.getSeat().getId() == null)
            return null;

        return seatService.findById(visit.getSeat().getId());
    }

    @ApiOperation(value = "get visit by Id", notes = "get visit info by id")
    @ApiImplicitParam(name = "id", value = "visit Id", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Visit read(HttpServletRequest req, @PathVariable Long id) {
        Visit visit = visitService.findById(id);
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
        if (!isEmpty(admin) && isAgencyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
            if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
                String timezone = DateUtils.get_timezone(adminAgencyCompanyOrganization.getAgency().getTimezone());
                visit = get_object_with_local_times(visit, timezone);
            }
        }
        return visit;
    }

    @ApiOperation(value = "update visit", notes = "update a visit")
    @ApiParam(name = "visit", value = "visit to update", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Visit> update(HttpServletRequest req, @RequestBody VisitUpdateRequest request) {
        Visit visit = request.visit;
        Visit visitDb = visitService.findById(visit.getId());
        String pin = getDigitPin(visit.getPin());

        User user = getUser(pin, visit.getAgency());
        visit.setUser(user);
        visit.setSeat(getSeat(visit));
        if (visitDb.getSeat() != null && (visit.getSeat() == null || !visitDb.getSeat().getId().equals(visit.getSeat().getId()))) {
            visitDb.getSeat().setStatus(SeatStatusEnum.AVAILABLE);
            visitDb.getSeat().setUser(null);
            seatService.save(visitDb.getSeat());
        }
        if (visit.getSeat() != null) {
            visit.getSeat().setStatus(SeatStatusEnum.TAKEN);
            visit.getSeat().setUser(user);
            visit.setSeat(seatService.save(visit.getSeat()));
        }

        visit = visitService.save(visit);
        return new ResponseEntity<Visit>(visit, HttpStatus.OK);
    }

    private User getUser(String pin, Agency agency) {
        if (agency == null || agency.getId() == null) {
            throw new UserException("Please provide agency");
        }

        User user = userService.getUserByPIN(pin, agency.getId());

        if (user == null) {
            throw new UserException("No user found with this PIN.");
        }

        return user;
    }

    @ApiOperation(value = "delete visit", notes = "delete a visit")
    @ApiImplicitParam(name = "id", value = "visit Id", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        visitService.deleteById(id);
    }

    @ApiOperation(value = "check in", notes = "check in")
    @ApiParam(name = "visitRequest", value = "visit Id or customPin", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @RequestMapping(value = "/checkin", method = RequestMethod.PUT)
    public ResponseEntity<Visit> checkIn(@RequestBody VisitRequest visitRequest) {
        if (visitRequest.getId() != null || visitRequest.getCustomPin() != null) {

            String pin = getDigitPin(visitRequest.getCustomPin());
            visitRequest.setCustomPin(pin);

            Visit visit = visitService.checkIn(visitRequest);
            return new ResponseEntity<Visit>(visit, HttpStatus.OK);
        } else {
            return new ResponseEntity<Visit>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "mobile check in", notes = "mobile check in")
    @ApiParam(name = "MobileCheckinRequestDTO", value = "MobileCheckinRequestDTO", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @RequestMapping(value = "/mobile/checkin", method = RequestMethod.POST)
    public ResponseEntity<MobileCheckinRequestDTO> mobileCheckIn(@RequestBody MobileCheckinRequestDTO mobileCheckinRequestDTO) {
        Visit visit = new Visit();

        String pin = getDigitPin(mobileCheckinRequestDTO.getPin());
        if (pin == null) {
            return new ResponseEntity<MobileCheckinRequestDTO>(mobileCheckinRequestDTO, HttpStatus.BAD_REQUEST);
        }
        visit.setPin(pin);
        mobileCheckinRequestDTO = visitService.mobileCheckIn(mobileCheckinRequestDTO, visit);
        return new ResponseEntity<MobileCheckinRequestDTO>(mobileCheckinRequestDTO, HttpStatus.OK);
    }


    @ApiOperation(value = "express check in", notes = "express check in")
    @ApiParam(name = "ExpressVisitRequestDTO", value = "Pin", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @RequestMapping(value = "/express/checkin", method = RequestMethod.POST)
    public ResponseEntity<ExpressVisitRequestDTO> expressCheckIn(@RequestBody ExpressVisitRequestDTO expressVisitRequestDTO) {
        Visit visit = new Visit();


        String pin = getDigitPin(expressVisitRequestDTO.getPin());
        if (pin == null) {
            return new ResponseEntity<ExpressVisitRequestDTO>(expressVisitRequestDTO, HttpStatus.BAD_REQUEST);
        }
        visit.setPin(pin);
        expressVisitRequestDTO = visitService.expressCheckin(expressVisitRequestDTO, visit);
        return new ResponseEntity<ExpressVisitRequestDTO>(expressVisitRequestDTO, HttpStatus.OK);
    }

    private String getDigitPin(String pin) {
        if (pin == null || pin.isEmpty())
            return null;
        // This is to handle online pinOrBarcode which in format PIN-Firstname
        // lastname
        if (pin.contains("-")) {
            return pin.substring(0, pin.indexOf("-"));
        }

        return pin;
    }

    @ApiOperation(value = "express check out", notes = "express check out")
    @ApiParam(name = "ExpressVisitRequestDTO", value = "Pin", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @RequestMapping(value = "/express/checkout", method = RequestMethod.PUT)
    public ResponseEntity<ExpressVisitRequestDTO> expressCheckOut(@RequestBody ExpressVisitRequestDTO expressVisitRequestDTO) {
        if (expressVisitRequestDTO.getPin() != null && !expressVisitRequestDTO.getPin().trim().equals("")) {
            String pin = getDigitPin(expressVisitRequestDTO.getPin());
            expressVisitRequestDTO.setPin(pin);
            expressVisitRequestDTO = visitService.expressCheckOut(expressVisitRequestDTO);
            return new ResponseEntity<ExpressVisitRequestDTO>(expressVisitRequestDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<ExpressVisitRequestDTO>(HttpStatus.BAD_REQUEST);
        }
    }


    @ApiOperation(value = "check out", notes = "check out")
    @ApiParam(name = "visitRequest", value = "visit Id or customPin", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @RequestMapping(value = "/checkout", method = RequestMethod.PUT)
    public ResponseEntity<Visit> checkOut(@RequestBody VisitRequest visitRequest) {
        if ((visitRequest.getId() != null && visitRequest.getId() > 0)
                || (visitRequest.getCustomPin() != null && !visitRequest.getCustomPin().trim().equals(""))) {

            String pin = getDigitPin(visitRequest.getCustomPin());
            visitRequest.setCustomPin(pin);

            Visit visit = visitService.checkOut(visitRequest);
            return new ResponseEntity<Visit>(visit, HttpStatus.OK);
        } else {
            return new ResponseEntity<Visit>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "get all visit", notes = "get all visit")
    @GetMapping()
    public ResponseEntity<Page<Visit>> readAll(@RequestParam MultiValueMap<String, String> attributes,
                                               HttpServletRequest req,
                                               @RequestParam(value = "status", required = false) List<String> status) {

        MultiValueMapConverter<Visit> converter = new MultiValueMapConverter<>(attributes, Visit.class);
        VisitRequestDTO dto = new VisitRequestDTO().fromMap(attributes);
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
        Page<Visit> visits = visitService.findAllVisitorByDate(dto.getFromDate(), dto.getToDate(), converter.getPageable(), status, dto.getStatFilter(), admin);
        if (!isEmpty(admin) && isAgencyAdmin(admin)) {
           AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
           if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
               String timezone = DateUtils.get_timezone(adminAgencyCompanyOrganization.getAgency().getTimezone());
               visits = get_local_time(visits.getContent(), timezone);
           }
        }
        return ResponseEntity.ok(visits);
    }

    @ApiOperation(value = "get all visit by service plan id", notes = "get all visit by service plan id")
    @ApiImplicitParam(name = "servicePlanId", value = "service plan id", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/serviceplan/{servicePlanId}", method = RequestMethod.GET)
    public List<Visit> findAllByServicePlanId(@PathVariable Long servicePlanId) {
        return visitService.findAllByServicePlanId(servicePlanId);
    }

    @ApiOperation(value = "get all visit by agency id", notes = "get all visit by agency id")
    @ApiImplicitParam(name = "agencyId", value = "agency id", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/{agencyId}/agency", method = RequestMethod.GET)
    public Page<Visit> findAllByAgencyId(@PathVariable Long agencyId, @RequestParam MultiValueMap<String, String> attributes) {
        MultiValueMapConverter<Visit> converter = new MultiValueMapConverter<>(attributes, Visit.class);
        return visitService.findAllByAgency(agencyId, converter.getPageable());
    }

    @ApiOperation(value = "get visit summary", notes = "get visit summary")
    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public List<VisitSummary> generateVisitSummary(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {
        VisitRequestDTO dto = new VisitRequestDTO().fromMap(attributes);
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
        return visitService.getAllVisitSummary(admin, dto.getFromDate());
    }

    @ApiOperation(value = "update visit meal summary", notes = "update visit meal summary")
    @RequestMapping(value = "/updatesummary", method = RequestMethod.POST)
    public Meal updateVisitMealSummary(@RequestBody Meal meal) {
        return visitService.updateVisitMealSummary(meal);
    }

    @ApiOperation(value = "get all visit report", notes = "get all visit report")
    @ApiParam(name = "visitReportRequest", value = "visitReportRequest", required = true)
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public List<VisitReportResponse> generateVisitReport(@RequestBody VisitReportRequest visitReportRequest) {
        return visitService.getAllVisitReport(visitReportRequest);
    }

    @ApiOperation(value = "get visit report", notes = "get visit report")
    @ApiParam(name = "basicReportFilterDTO", value = "basicReportFilterDTO", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @RequestMapping(value = "/visit_report", method = RequestMethod.POST)
    public Page<Visit> generateVisitReport(HttpServletRequest req,
                                           @RequestBody BasicReportFilterDTO basicReportFilterDTO,
                                           @RequestParam MultiValueMap<String, String> attributes) {
        req.getSession().setAttribute("visit_report", basicReportFilterDTO);
        Validator.validateCompanyAndAgency(basicReportFilterDTO);
        Validator.AUTH_ADMIN(req, basicReportFilterDTO);
        MultiValueMapConverter<Visit> converter = new MultiValueMapConverter<>(attributes, Visit.class);
        return visitService.getVisitReport(basicReportFilterDTO, converter.getPageable());
    }

    @ApiOperation(value = "download visit report", notes = "get visit report")
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping(value = "/report-download")
    public ResponseEntity<byte[]> downloadVisitReport(HttpServletRequest req,
                                                      BasicReportFilterDTO basicReportFilterDTO) {
        if (basicReportFilterDTO != null) {
            byte[] data = visitService.getVisitReportAndDownload(basicReportFilterDTO);
            return ResponseEntity.ok().contentLength(data.length)
                    .contentType(MediaType
                            .parseMediaType("application/pdf"))
                    .body(data);
        }
        return null;
    }

    @ApiOperation(value = "get visit report with senior stats", notes = "get visit report with senior stats")
    @ApiParam(name = "visitSeniorStatsReqDTO", value = "visitSeniorStatsReqDTO", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @RequestMapping(value = "/visit_report_senior_stats", method = RequestMethod.POST)
    public VisitSeniorStatsResDTO generateVisitReportSeniorStats(HttpServletRequest req,
                                                                 @RequestBody VisitSeniorStatsReqDTO visitSeniorStatsReqDTO) {

        validateInput(visitSeniorStatsReqDTO);

        Validator.validateCompanyAndAgency(visitSeniorStatsReqDTO);

        Validator.AUTH_ADMIN(req, visitSeniorStatsReqDTO);

        return visitService.getVisitReportSeniorsStats(visitSeniorStatsReqDTO);
    }

    private void validateInput(VisitSeniorStatsReqDTO visitSeniorStatsReq) {
        if (!isNull(visitSeniorStatsReq.getUserId())) {
            if (visitSeniorStatsReq.getSeniorStatsEnum() == null) {
                throw new UserException(
                        "Please select any one, Senior OR [All seniors , Active seniors , Left seniors etc].");
            }
        }
    }

    @ApiOperation(value = "get billing report", notes = "get billing report")
    @ApiParam(name = "billingReportRequest", value = "billingReportRequest", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @RequestMapping(value = "/billing_report", method = RequestMethod.POST)
    public BillingReportResponseDTO generateBillingReport(HttpServletRequest req,
                                                          @RequestBody BasicReportFilterDTO basicFilter) {

        Validator.validateCompanyAndAgency(basicFilter);

        Validator.AUTH_ADMIN(req, basicFilter);

        SET_DATE_RANGE_IF_NOT_PROVIDED(basicFilter);

        List<HealthInsuranceClaim> healthInsuranceClaimList = healthInsuranceClaimService
                .findHealthInsuranceReport(basicFilter);

        List<VisitDTO> visitList = visitService.getVisitReport(basicFilter);

        ReportDTO report = getReport(req, basicFilter);

        BillingReportResponseDTO responseDTO = new BillingReportResponseDTO();
        responseDTO.setHealthInsuranceClaimList(healthInsuranceClaimList);
        responseDTO.setVisitList(visitList);
        responseDTO.setReport(report);

        return responseDTO;

    }

    private ReportDTO getReport(HttpServletRequest req, @RequestBody BasicReportFilterDTO basicFilter) {
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

        Report report = new Report();
        report.setAdmin(admin);

        Company company = new Company();
        company.setId(basicFilter.getCompanyId());

        report.setCompany(company);
        report.setBaseId(basicFilter.getAgencyId());
        report.setReportTitle("Billing claim report");
        report.setStartDate(new Timestamp(basicFilter.getStartDate().getTime()));
        report.setEndDate(new Timestamp(basicFilter.getEndDate().getTime()));
        report.setDataColumns("Visit Records | Form 1500");
        report.setFormat("Pdf");
        report.setRole(admin.getRole());

        report = reportService.save(report);

        ReportDTO reportDTO = modelMapper.map(report, ReportDTO.class);

        return reportDTO;
    }

    @ApiOperation(value = "disable visit", notes = "disable a visit")
    @ApiImplicitParam(name = "id", value = "visit Id", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/{id}/disable", method = RequestMethod.PUT)
    public void disable(@PathVariable Long id) {
        visitService.disableById(id);
    }

    @ApiOperation(value = "bill visit", notes = "bill a visit")
    @RequestMapping(value = "/bill", method = RequestMethod.PUT)
    public void disable(@RequestParam("visiId") String id, @RequestParam("billingCode") String billingCode) {
        visitService.billVisit(billingCode, Long.valueOf(id));
    }

    @ApiOperation(value = "get all visit by range time", notes = "get all visit by range time")
    @ApiImplicitParam(name = "startDate", value = "start date and format", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/range", method = RequestMethod.POST)
    public VisitStatusDTO findAllByTimeRange(HttpServletRequest req,
                                             @RequestBody VisitRequestDTO dto,
                                             @RequestParam MultiValueMap<String, String> attributes) {
        MultiValueMapConverter<Ride> converter = new MultiValueMapConverter<>(attributes, Ride.class);
        //VisitRequestDTO dto = new VisitRequestDTO().fromMap(attributes);
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
        if (HealthcareUtil.isEmpty(admin)){
            throw new UserException(NOT_AUTHORIZED);
        }
        VisitStatusDTO visitStatusDtos = visitService.getAuthorizedVisitors(admin, dto.getFromDate(), dto.getToDate(), dto.getAgencyIds(), dto.getSeniorIds(),
                                                converter.getPageable(), dto.isInvalid());

        return visitStatusDtos;
    }

    @ApiOperation(value = "download visit report", notes = "get visit report")
    @ApiImplicitParam(name = "startDate", value = "start date and format", required = true, dataType = "Long", paramType = "path")
    @GetMapping(value = "/billing-report-download")
    public ResponseEntity<byte[]> downloadVisitReport(HttpServletRequest req,
                                                      VisitRequestDTO visitRequestDTO) {


        if (visitRequestDTO != null) {
            if (visitRequestDTO.getIsValidVisitor() == null) {
                visitRequestDTO.setIsValidVisitor(0);
            }
            byte[] data = visitService.getVisitBillingReportAndDownload(visitRequestDTO, req);
            return ResponseEntity.ok().contentLength(data.length)
                    .contentType(MediaType
                            .parseMediaType("application/pdf"))
                    .body(data);
        }
        return null;
    }

    @RequestMapping(value = "/get-statistics", method = RequestMethod.GET)
    public Map<String, Long> getStatistics(HttpServletRequest req, @RequestParam(value="fromDate", required = false) String fromDate) {
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
        if (isEmpty(admin)) {
            throw new UserException(NOT_AUTHORIZED);
        }
        Map<String, Long> stats = visitService.getStatistics(admin, fromDate == null ? new Date() : new DateTime(fromDate).toDate());
        return stats;
    }

	@ApiOperation(value = "checked in", notes = "checked in visit")
	@RequestMapping(value="/checked-in", method = RequestMethod.POST)
	public ResponseEntity<Boolean> isCheckedIn(HttpServletRequest req, @RequestBody Visit request) {
		Visit visit = request;
		String pin = getDigitPin(visit.getPin());
		if (pin == null) {
			return ResponseEntity.ok(false);
		}

		if (visit.getAgency() == null || visit.getAgency().getId() == null) {
			return ResponseEntity.ok(false);
		}

		User user = userService.getUserByPIN(pin, visit.getAgency().getId());

		if (user == null) {
			return ResponseEntity.ok(false);
		}

		List<Visit> visitors = visitService.findByAgencyUser(visit.getAgency().getId(), user.getId());
		if (visitors != null && visitors.size() > 0) {
			for (Visit v : visitors) {
				if (VisitStatusEnum.CHECK_IN.equals(VisitStatusEnum.valueOf(v.getStatus()))) {
					return ResponseEntity.ok(true);
				}
			}
		}

		return ResponseEntity.ok(false);
	}

    public Page<Visit> get_local_time(List<Visit> visits, String target_time_zone){
        for(Visit visit : visits){
            visit = get_object_with_local_times(visit, target_time_zone);
        }

        return new PageImpl<Visit>(visits);
    }

    public Visit get_object_with_local_times(Visit visit, String target_time_zone){
        if(visit.getCheckInTime() != null){
            visit.setCheckInTime(DateUtils.get_local_time(visit.getCheckInTime(), target_time_zone));
        }
       if(visit.getCheckOutTime() != null){
            visit.setCheckOutTime(DateUtils.get_local_time(visit.getCheckOutTime(), target_time_zone));

       }
       return visit;
}

	@ApiOperation(value = "checked in", notes = "checked in visit")
	@RequestMapping(value="/is-over-limitation", method = RequestMethod.POST)
	public ResponseEntity<Boolean> isLimitation(HttpServletRequest req, @RequestBody Visit request) {
		Visit visit = request;
		String pin = getDigitPin(visit.getPin());
		if (pin == null) {
			return ResponseEntity.ok(false);
		}

		if (visit.getAgency() == null || visit.getAgency().getId() == null) {
			return ResponseEntity.ok(false);
		}

		User user = userService.getUserByPIN(pin, visit.getAgency().getId());

		if (user == null) {
			return ResponseEntity.ok(false);
		}

		// get date by today
		DateTime today = new DateTime();
		DateTime startDate = today.withTimeAtStartOfDay();
		DateTime endDate  = startDate.plusDays(1);

		List<Visit> visted = visitService.findAllOnlyCheckedInVisitCount(user.getAgency().getId(), startDate.toDate(), endDate.toDate());

		if (visted != null && user.getAgency().getCapacityNum() != null && visted.size() >= user.getAgency().getCapacityNum()) {
			return ResponseEntity.ok(true);
		}

		return ResponseEntity.ok(false);
	}

    @ApiOperation(value = "download meal order report", notes = "get meal order report")
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping(value = "/mealorder-report-download")
    public ResponseEntity<byte[]> downloadMealOrderReport(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {
        VisitRequestDTO dto = new VisitRequestDTO().fromMap(attributes);
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
        byte[] data = visitService.generateAndDownloadMealOrderReport(admin, dto.getFromDate());
        return ResponseEntity.ok().contentLength(data.length).contentType(MediaType.parseMediaType("application/pdf")).body(data);
    }

    @ApiOperation(value = "update visit based on meal order", notes = "update visit based on meal order")
    @RequestMapping(value = "/update-meal-order", method = RequestMethod.POST)
    public ResponseEntity<VisitUpdateResponse> updateVisitMealOrder(HttpServletRequest req, @RequestBody VisitUpdateRequest request) {

        // 0 for pin is null
        // 1 for agency is empty
        // 2 user is null
        // 3 is checkout
        // 4 is check in
        // 5 reserved
        // 6 not found record
        // 7 Food Allergies
        return updateVisitMeal(request);
    }

    @ApiOperation(value = "update visit based on meal order", notes = "update visit based on meal order")
    @RequestMapping(value = "/mobile/update-meal-order", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> updateVisitMealOrderFromMobile(HttpServletRequest req, @RequestBody Visit request) {

        JSONObject entity = new JSONObject();
        try {
            updateMeal(request);
            entity.put("result", "SUCCESS");
            entity.put("data", "");
            entity.put("message", "");
            return new ResponseEntity<JSONObject>(entity, HttpStatus.OK);
        } catch (Exception e) {
            entity.put("result", "ERROR");
            entity.put("data", new JSONObject());
            entity.put("message", "Invalid Input");
            return new ResponseEntity<JSONObject>(entity, HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<Integer> updateMeal(@RequestBody Visit request) {
        Visit visit = request;
        String pin = getDigitPin(visit.getPin());
        if (pin == null) {
            return ResponseEntity.ok(0);
        }

        if (visit.getAgency() == null || visit.getAgency().getId() == null) {
            return ResponseEntity.ok(1);
        }

        User user = userService.getUserByPIN(pin, visit.getAgency().getId());

        if (user == null) {
            return ResponseEntity.ok(2);
        }

        List<Visit> visitors = visitService.findByAgencyUser(visit.getAgency().getId(), user.getId());

        if (visitors != null && visitors.size() > 0) {
            List<Visit> checkInVisits = visitors.stream().filter(v -> VisitStatusEnum.CHECK_IN.equals(VisitStatusEnum.valueOf(v.getStatus())))
                                            .collect(Collectors.toList());
            for (Visit v : checkInVisits) {
                if(request.getSelectedBreakfast() != null &&  request.getSelectedBreakfast().getId() != null) {
                    v.setSelectedBreakfast(request.getSelectedBreakfast());
                }
                if(request.getSelectedDinner() != null &&  request.getSelectedDinner().getId() != null) {
                    v.setSelectedDinner(request.getSelectedDinner());
                }
                if(request.getSelectedLunch() != null &&  request.getSelectedLunch().getId() != null) {
                    v.setSelectedLunch(request.getSelectedLunch());
                }
                visitService.save(v);
                return ResponseEntity.ok(4);
            }
            for (Visit v : visitors) {
                if (VisitStatusEnum.CHECK_OUT.equals(VisitStatusEnum.valueOf(v.getStatus()))) {
                    return ResponseEntity.ok(3);
                }

                if (VisitStatusEnum.RESERVED.equals(VisitStatusEnum.valueOf(v.getStatus()))) {
                    // store update visit
                    v.setSelectedBreakfast(request.getSelectedBreakfast());
                    v.setSelectedDinner(request.getSelectedDinner());
                    v.setSelectedLunch(request.getSelectedLunch());
                    visitService.save(v);
                    return ResponseEntity.ok(5);
                }
            }
        }

        return ResponseEntity.ok(6);
    }

   /**
   * This method check whether user have food allergies
   * @param user
   * @param meals
   * @return
   */

    public Map<String, List<String>> checkMealAllergy(User user, List<Meal> meals) {
      Map<String, List<String>> response = new HashMap<>();
      List<FoodAllergy> userFoodAllergies = user.getFoodAllergies();
        for (Meal meal : meals) {
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

  /**
   *
   * @param request
   * @return VisitUpdateResponse
   */

  public ResponseEntity<VisitUpdateResponse> updateVisitMeal(VisitUpdateRequest request) {

    VisitUpdateResponse response = new VisitUpdateResponse();
    Visit visit = request.visit;
    String pin = getDigitPin(visit.getPin());
    if (pin == null) {
      response.status = 0;
      return ResponseEntity.ok(response);
    }
    if (visit.getAgency() == null || visit.getAgency().getId() == null) {
      response.status = 1;
      return ResponseEntity.ok(response);
    }

    User user = userService.getUserByPIN(pin, visit.getAgency().getId());

    if (user == null) {
      response.status = 2;
      return ResponseEntity.ok(response);
    }

    List<Visit> visitors = visitService.findByAgencyUser(visit.getAgency().getId(), user.getId());

    if (visitors != null && visitors.size() > 0) {
      for (Visit v : visitors) {
          if (VisitStatusEnum.CHECK_OUT.equals(VisitStatusEnum.valueOf(v.getStatus()))) {
            response.status = 3;
            return ResponseEntity.ok(response);
          }
          List<Meal> meals = new ArrayList<>();

          Meal breakfast = null;
          Meal lunch = null;
          Meal dinner = null;

          if(visit.getSelectedBreakfast() != null && visit.getSelectedBreakfast().getId() != null) {
            breakfast = mealService.findById(visit.getSelectedBreakfast().getId());
            meals.add(breakfast);
          }
          if(visit.getSelectedDinner() != null && visit.getSelectedDinner().getId() !=  null) {
            dinner = mealService.findById(visit.getSelectedDinner().getId());
            meals.add(dinner);
          }
          if(visit.getSelectedLunch() != null && visit.getSelectedLunch().getId() != null) {
            lunch = mealService.findById(visit.getSelectedLunch().getId());
            meals.add(lunch);
          }

          if(!request.skipAllergies) {
            Map<String, List<String>> userAlleries =  checkMealAllergy(user, meals);
            if(!userAlleries.isEmpty()) {
              response.status = 7;
              response.content = userAlleries;
              return  ResponseEntity.ok(response);
            }
          }

          if (VisitStatusEnum.CHECK_IN.equals(VisitStatusEnum.valueOf(v.getStatus()))) {
            // store update meal id
            v.setSelectedBreakfast(breakfast);
            v.setSelectedDinner(dinner);
            v.setSelectedLunch(lunch);
            visitService.save(v);
            response.status = 4;
            return ResponseEntity.ok(response);
          }

          if (VisitStatusEnum.RESERVED.equals(VisitStatusEnum.valueOf(v.getStatus()))) {
            // store update visit
            v.setSelectedBreakfast(breakfast);
            v.setSelectedDinner(dinner);
            v.setSelectedLunch(lunch);
            visitService.save(v);
            response.status = 5;
            return  ResponseEntity.ok(response);
          }
       }
     }
     response.status = 6;
     return ResponseEntity.ok(response);
    }
  }
