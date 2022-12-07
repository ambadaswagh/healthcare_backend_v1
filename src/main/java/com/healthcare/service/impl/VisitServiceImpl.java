package com.healthcare.service.impl;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareConstants.NOT_AUTHORIZED;
import static com.healthcare.api.common.HealthcareUtil.SET_DATE_RANGE_IF_NOT_PROVIDED;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.HealthcareUtil.isNull;
import static com.healthcare.model.enums.SeniorStatsEnum.ActiveSeniors;
import static com.healthcare.model.enums.SeniorStatsEnum.AllSeniors;
import static com.healthcare.model.enums.SeniorStatsEnum.LeftSeniors;
import static com.healthcare.model.enums.SeniorStatsEnum.NotStartSeniors;
import static com.healthcare.model.enums.StatusEnum.ACTIVE;
import static com.healthcare.model.enums.StatusEnum.INACTIVE;
import static com.healthcare.model.enums.StatusEnum.REGISTERED;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.dto.*;
import com.healthcare.exception.HealthcareApplicationException;
import com.healthcare.exception.UserException;
import com.healthcare.model.entity.*;
import com.healthcare.api.common.HealthcareUtil;
import com.healthcare.api.model.VisitReportRequest;
import com.healthcare.api.model.VisitReportResponse;
import com.healthcare.api.model.VisitRequest;
import com.healthcare.api.model.VisitSummary;
import com.healthcare.exception.ApplicationException;
import com.healthcare.model.response.Response;
import com.healthcare.repository.AdminAgencyCompanyOrganizationRepository;
import com.healthcare.repository.AdminSettingRepository;
import com.healthcare.repository.UserRepository;
import com.healthcare.repository.VisitRepository;
import com.healthcare.service.*;
import io.jsonwebtoken.lang.Collections;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.healthcare.api.model.ExpressVisitRequestDTO;
import com.healthcare.api.model.MobileCheckinRequestDTO;
import com.healthcare.model.enums.EntityStatusEnum;
import com.healthcare.model.enums.FixedStatusEnum;
import com.healthcare.model.enums.PeriodTypeEnum;
import com.healthcare.model.enums.SeatStatusEnum;
import com.healthcare.model.enums.StatusEnum;
import com.healthcare.model.enums.UnitTypeEnum;
import com.healthcare.model.enums.VisitStatusEnum;
import com.healthcare.model.enums.VisitTypeEnum;

import java.util.stream.Collectors;

import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

@Service
@Transactional
public class VisitServiceImpl extends BasicService<Visit, VisitRepository> implements VisitService {
	private static final String KEY = Visit.class.getSimpleName();

	@Autowired
	public VisitRepository visitRepository;
	@Autowired
	public BillingService billingService;
	@Autowired
	public UserRepository userRepository;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private AgencyService agencyService;
	@Autowired
	private UserService userService;
	@Autowired
	private ReportService reportService;
	@Autowired
	private SeatService seatService;
	@Autowired
	private TableService tableService;
	@Autowired
	private MealService mealService;

	@Autowired
	private AdminSettingRepository adminSettingRepository;

	@Autowired
	private AdminAgencyCompanyOrganizationRepository adminAgencyCompanyOrganizationRepository;

	@Autowired
	private RedisTemplate<String, Visit> redisTemplate;

	@Autowired
  private ObjectMapper objectMapper;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	@Transactional
	public Visit save(Visit visit) {
		if(visit.getId() != null){
			visit.setUpdatedAt(new Timestamp(new Date().getTime()));
    	} else {
    		visit.setCreatedAt(new Timestamp(new Date().getTime()));
    		visit.setUpdatedAt(new Timestamp(new Date().getTime()));
    	}
		visit = visitRepository.save(visit);
		redisTemplate.opsForHash().put(KEY, visit.getId(), visit);
		return visit;
	}

    @Override @Transactional
    public Visit findById(Long id) {
	       String idStr = String.valueOf(id);
        /*if (redisTemplate.opsForHash().hasKey(KEY, idStr)) {
          String result = (String) redisTemplate.opsForHash().get(KEY, idStr);
          try {
            Visit visit = objectMapper.readValue(result, Visit.class);
            Meal breakfast = visit.getSelectedBreakfast();
            if(breakfast != null)
              visit.setSelectedBreakfast(mealService.findById(breakfast.getId()));
            Meal lunch = visit.getSelectedLunch();
            if(lunch != null)
              visit.setSelectedLunch(mealService.findById(lunch.getId()));
            Meal dinner = visit.getSelectedDinner();
            if(dinner != null)
              visit.setSelectedDinner(mealService.findById(dinner.getId()));
            return visit;
          } catch (IOException e) {
           logger.error(e.getMessage());
          }
        }*/
        Visit visit = visitRepository.findOne(id);
        return visit;
    }

  private Visit saveInRedis(Visit visit) {
    try {
      String res = objectMapper.writeValueAsString(visit);
      redisTemplate.opsForHash().put(KEY, String.valueOf(visit.getId()), res);
    } catch (JsonProcessingException e) {
      logger.error(e.getMessage());
    }
    return visit;
  }

  @Override
	@Transactional
	public Long deleteById(Long id) {
		visitRepository.delete(id);
		return redisTemplate.opsForHash().delete(KEY, id);
	}

	@Override
	@Transactional
	public Visit checkIn(VisitRequest visitRequest) {
		Visit visit = null;
		if (visitRequest.getId() != null && visitRequest.getId() > 0) {
			visit = findById(visitRequest.getId());
		} else {
			DateTime today = new DateTime();
			List<Visit> list = visitRepository.findByPin(visitRequest.getCustomPin(), today.toDate());
			visit = ((list != null && list.size() > 0) ? list.get(0) : null);
		}

		// if no visit found
		if (visit == null) {
			throw new ApplicationException(Response.ResultCode.VISIT_NOT_FOUND,
					"Visit not found,Please provide visit Id");
		}

		// check in
		if (VisitStatusEnum.RESERVED.equals(VisitStatusEnum.valueOf(visit.getStatus()))) {
			visit.setCheckInTime(new Timestamp(new Date().getTime()));
			visit.setStatus(VisitStatusEnum.CHECK_IN.toString());
		} else {
			throw new ApplicationException(Response.ResultCode.INVALID_STATUS, "Invalid visit status");
		}
		// save visit
		return save(visit);
	}

	@Override
	@Transactional
	public Visit checkOut(VisitRequest visitRequest) {
		Visit visit = null;
		if (visitRequest.getId() != null && visitRequest.getId() > 0) {
			visit = findById(visitRequest.getId());
		} else {
			DateTime today = new DateTime();
			DateTime startDate = today.withTimeAtStartOfDay();
			DateTime endDate = startDate.plusDays(1);
			visit = visitRepository.findByPinDateAgency(visitRequest.getCustomPin(), startDate.toDate(), endDate.toDate(),visitRequest.getAgencyId());
		}
		// if no visit found
		if (visit == null) {
			throw new HealthcareApplicationException(Response.ResultCode.VISIT_NOT_FOUND, "Visit not found, Please provide visit id.", HttpStatus.OK);
		}
		// check out
		if (VisitStatusEnum.CHECK_IN.equals(VisitStatusEnum.valueOf(visit.getStatus()))) {
			visit.setCheckOutTime(new Timestamp(new Date().getTime()));
			visit.setStatus(VisitStatusEnum.CHECK_OUT.toString());

			Seat seat = visit.getSeat();
			if(seat!=null)
			{
				seat.setUser(null);
				seat.setStatus(SeatStatusEnum.AVAILABLE);
				seatService.save(seat);
			}
		} else {
			throw new HealthcareApplicationException(Response.ResultCode.VISIT_NOT_FOUND, "Visit not found, Please provide visit id.", HttpStatus.OK);
		}
		// save visit
		return save(visit);
	}

	@Override
	@Transactional
	public List<Visit> findAllByServicePlanId(Long servicePlanId) {
		return visitRepository.findAllByServicePlanId(servicePlanId);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Visit> findAll() {
		Map<Object, Object> visitMap = redisTemplate.opsForHash().entries(KEY);
		List<Visit> visitList = Collections.arrayToList(visitMap.values().toArray());
		if (visitMap.isEmpty())
			visitList = visitRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
		return visitList;
	}

	@Override
	@Transactional
	public List<VisitReportResponse> getAllVisitReport(VisitReportRequest visitReportRequest) {
		List<VisitReportResponse> report = new ArrayList<VisitReportResponse>();
		List<Object[]> result = visitRepository.findAllInPeriod(visitReportRequest.getStartDate(),
				visitReportRequest.getEndDate());
		for (Object[] resObj : result) {
			VisitReportResponse response = new VisitReportResponse();
			response.setUserName((String) resObj[0]);
			response.setNoOfVisit((BigInteger) resObj[1]);
			response.setAverageHour((BigDecimal) resObj[2]);
			report.add(response);
		}
		return report;
	}

	public Page<Visit> findAll(Pageable pageable, List<String> status){

		String checkIn = "CHECK_IN";
		String checkOut = "CHECK_OUT";
		String reserved = "RESERVED";
		String stringStatus = null;
		if(!CollectionUtils.isEmpty(status)) {
			if(!status.contains(checkIn)) {
				checkIn = "dummy";
			}
			if(!status.contains(checkOut)) {
				checkOut = "dummy";
			}
			if(!status.contains(reserved)) {
				reserved = "dummy";
			}
		}
		return visitRepository.findAll(pageable, checkIn, checkOut, reserved);
	}

	@Override
	@Transactional
	public List<VisitSummary> getAllVisitSummary(Admin permissionAdmin, String startDate) {
		List<VisitSummary> summary = new ArrayList<VisitSummary>();
		DateTime currentTime = new DateTime(startDate);
		DateTime endTime   = currentTime.plusHours(24);

		List<Object[]> result = new ArrayList<Object[]>();

		if (isSuperAdmin(permissionAdmin)) {
			result = visitRepository.findAllVisitSummary(currentTime.toDate(), endTime.toDate());
		} else if (isCompanyAdmin(permissionAdmin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

			if (adminAgencyCompanyOrganization != null) {

				List<Agency> agencies = agencyService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId());
				if (agencies != null && agencies.size() > 0) {
					List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
					result = visitRepository.findAllVisitSummaryByCompany(currentTime.toDate(), endTime.toDate(), ids);
				}
			}
		} else if (isAgencyAdmin(permissionAdmin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				result = visitRepository.findAllVisitSummaryByAgency(currentTime.toDate(), endTime.toDate(), adminAgencyCompanyOrganization.getAgency().getId());
			}
		}

		for (Object[] resObj : result) {
			VisitSummary response = new VisitSummary();

			response.setId((Integer) resObj[0]);
			response.setRestaurantName((String)resObj[1]);
			response.setMealName((String)resObj[2]);
			response.setMealClass((String)resObj[3]);
			response.setQuantity((BigInteger)resObj[4]);
			response.setStatus((String)resObj[5]);
			response.setMealStatus((Integer)resObj[6]);

			summary.add(response);
		}
		return summary;
	}

	@Override
	@Transactional
	public List<VisitDTO> getVisitReport(BasicReportFilterDTO req) {
		// SET_DATE_RANGE_IF_NOT_PROVIDED(req);

		if (isNull(req.getUserId())) {
			req.setUserId(null);
		}
		List<Visit> list = visitRepository.findVisitReport(req.getCompanyId(), req.getAgencyId(), req.getStartDate(),
				req.getEndDate(), req.getUserId());
		return HealthcareUtil.convertListToDTO(list);
	}

	@Override
	@Transactional
	public Page<Visit> getVisitReport(BasicReportFilterDTO req, Pageable pageable) {
		if (isNull(req.getUserId())) {
			req.setUserId(null);
		}
		return visitRepository.findVisitReport(req.getCompanyId(), req.getAgencyId(), req.getStartDate(),
				req.getEndDate(), req.getUserId(), pageable);
	}

	@Override
	@Transactional
	public byte[] getVisitReportAndDownload(BasicReportFilterDTO req) {
		if (isNull(req.getUserId())) {
			req.setUserId(null);
		}
		List<Visit> visits = visitRepository.findVisitReport(req.getCompanyId(), req.getAgencyId(), req.getStartDate(),
				req.getEndDate(), req.getUserId());
		Company company = companyService.findById(req.getCompanyId());
		Agency agency = agencyService.findById(req.getAgencyId());
		User user = null;
		if (req.getUserId() != null) {
			user = userService.findById(req.getUserId());
		}
		try {
			File file = reportService.generateVisitReport(new VisitReportContext(visits, company, agency, user));
			return IOUtils.toByteArray(new FileInputStream(file));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	@Transactional
	public byte[] getVisitBillingReportAndDownload(VisitRequestDTO reg, HttpServletRequest request) {

		Admin admin = (Admin) request.getAttribute(AUTHENTICATED_ADMIN);

		if (isEmpty(admin)) {
			throw new UserException(NOT_AUTHORIZED);
		}
		VisitStatusDTO visitStatusDtos = this.getAuthorizedVisitors(admin, reg.getFromDate(), reg.getToDate(), reg.getAgencyIds(), reg.getSeniorIds(), null, false);

		try {
			File file = reportService.generateVisitBillingReport(visitStatusDtos, reg.getIsValidVisitor(), request);
			return IOUtils.toByteArray(new FileInputStream(file));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	@Transactional
	public Long disableById(Long id) {
		Visit visit = null;
		if (redisTemplate.opsForHash().hasKey(KEY, id))
			visit = (Visit) redisTemplate.opsForHash().get(KEY, id);
		else
			visit = visitRepository.findOne(id);
		if (visit != null && visit.getId() != null) {
			visit.setActive(EntityStatusEnum.DISABLE.getValue());
			visit = visitRepository.save(visit);
			redisTemplate.opsForHash().put(KEY, visit.getId(), visit);
			return visit.getId();
		}
		return null;
	}

	@Override
	@Transactional
	public VisitSeniorStatsResDTO getVisitReportSeniorsStats(VisitSeniorStatsReqDTO req) {
		SET_DATE_RANGE_IF_NOT_PROVIDED(req);

		List<User> allSeniorsList = userRepository.findAllSeniorsForCompanyAndAgency(req.getCompanyId(),
				req.getAgencyId());

		SeniorStatsInfo seniorStatsInfo = new SeniorStatsInfo(allSeniorsList).process();
		Set<Long> activeSeniors = seniorStatsInfo.getActiveSeniors();
		Set<Long> registerSeniors = seniorStatsInfo.getRegisterSeniors();
		Set<Long> leftSeniors = seniorStatsInfo.getLeftSeniors();

		VisitSeniorStatsResDTO response = new VisitSeniorStatsResDTO();
		response.setAllSeniors(registerSeniors.size() + activeSeniors.size());
		response.setActiveSeniors(activeSeniors.size());
		response.setLeftSeniors(leftSeniors.size());
		response.setNotStartedSeniors(registerSeniors.size());
		response.setRetentionRate(calculateRetentionRate(registerSeniors, activeSeniors));
		populateResponse(req, response);

		return response;
	}

	@Override
	public Visit billVisit(String billingCode, Long visiId) {
		Visit visit = findById(visiId);
		if (visit != null) {
			visit.setBillingCode(billingCode);
			visit = save(visit);
		} else {
			throw new ApplicationException(Response.ResultCode.VISIT_NOT_FOUND,
					"Visit not found,Please provide visit Id");
		}
		return visit;
	}

    @Override
    public List<Visit> findAllByAgencyId(Long agencyId) {
        return visitRepository.findAllByAgencyId(agencyId);
    }

    private double calculateRetentionRate(Set<Long> allRegisterSeniorsSet, Set<Long> allActiveSeniorsSet) {
		return ((double) ((double) allActiveSeniorsSet.size()
				/ (double) (allActiveSeniorsSet.size() + allRegisterSeniorsSet.size())) * 100);
	}

	private void populateResponse(VisitSeniorStatsReqDTO req, VisitSeniorStatsResDTO response) {

		if (AllSeniors.equals(req.getSeniorStatsEnum())) {

			populate(req, response, Arrays.asList(REGISTERED, ACTIVE));

		} else if (ActiveSeniors.equals(req.getSeniorStatsEnum())) {

			populate(req, response, Arrays.asList(ACTIVE));

		} else if (LeftSeniors.equals(req.getSeniorStatsEnum())) {

			populate(req, response, Arrays.asList(INACTIVE));

		} else if (NotStartSeniors.equals(req.getSeniorStatsEnum())) {
			// No record in visit table, so return empty list.
		}
	}

	private List<Visit> populate(VisitSeniorStatsReqDTO req, VisitSeniorStatsResDTO response,
			List<StatusEnum> statusIds) {
		List<Visit> visitReportList;

		visitReportList = visitRepository.findAllSeniorsVisit(req.getCompanyId(), req.getAgencyId(), req.getStartDate(),
				req.getEndDate(), statusIds);

		Long completeCheckInCheckOutCount = visitRepository.findAllCompleteCheckedInOutVisitCount(req.getCompanyId(),
				req.getAgencyId(), req.getStartDate(), req.getEndDate(), Arrays.asList(REGISTERED, ACTIVE));

		Long onlyCheckInCount = visitRepository.findAllOnlyCheckedInVisitCount(req.getCompanyId(), req.getAgencyId(),
				req.getStartDate(), req.getEndDate(), Arrays.asList(REGISTERED, ACTIVE));

		response.setVisits(HealthcareUtil.convertListToDTO(visitReportList));
		response.setCompletedCheckInOut(completeCheckInCheckOutCount);
		response.setOnlyCheckIn(onlyCheckInCount);

		return visitReportList;
	}

	private class SeniorStatsInfo {
		private List<User> allSeniors;
		private Set<Long> registerSeniors;
		private Set<Long> activeSeniors;
		private Set<Long> leftSeniors;

		public SeniorStatsInfo(List<User> allSeniors) {
			this.allSeniors = allSeniors;
		}

		public Set<Long> getRegisterSeniors() {
			return registerSeniors;
		}

		public Set<Long> getActiveSeniors() {
			return activeSeniors;
		}

		public Set<Long> getLeftSeniors() {
			return leftSeniors;
		}

		public SeniorStatsInfo process() {
			registerSeniors = new HashSet<Long>();
			activeSeniors = new HashSet<Long>();
			leftSeniors = new HashSet<Long>();

			for (User user : allSeniors) {
				switch (user.getStatus()) {
				case REGISTERED:
					registerSeniors.add(user.getId());
					break;
				case ACTIVE:
					activeSeniors.add(user.getId());
					break;
				case INACTIVE:
					leftSeniors.add(user.getId());
					break;
				}
			}
			return this;
		}
	}

	@Override
	public VisitStatusDTO getAuthorizedVisitors(Admin permissionAdmin, String startDate, String endDate, List<Long> agencyIds, List<Long> seniorIds,
												Pageable pageable, boolean isInvalid) {

		VisitStatusDTO outputVisitors = getData(permissionAdmin, startDate, endDate, agencyIds, seniorIds);

		if(pageable != null) {
			if(isInvalid) {
				List invalidVisitors = outputVisitors.getInValidVisitors();
				int start = invalidVisitors == null || invalidVisitors.isEmpty() ? 0 : pageable.getOffset();
				int end = (start + pageable.getPageSize()) > invalidVisitors.size() ? invalidVisitors.size() : (start + pageable.getPageSize());
				Page<VisitResponseDTO> pages = new PageImpl<VisitResponseDTO>(invalidVisitors.subList(start, end), pageable, invalidVisitors.size());
				outputVisitors.setInValidVisitorsPage(pages);
			} else {
				List validVisitors = outputVisitors.getValidVisitors();
				int start = validVisitors == null || validVisitors.isEmpty() ? 0 : pageable.getOffset();
				int end = (start + pageable.getPageSize()) > validVisitors.size() ? validVisitors.size() : (start + pageable.getPageSize());
				Page<VisitResponseDTO> pages = new PageImpl<VisitResponseDTO>(validVisitors.subList(start, end), pageable, validVisitors.size());
				outputVisitors.setValidVisitorsPage(pages);
			}
		}


		return outputVisitors;
	}

	private VisitStatusDTO getData(Admin permissionAdmin, String startDate, String endDate, List<Long> agencyIds, List<Long> seniorIds) {
		DateTime today = new DateTime(startDate);
		DateTime endDay = new DateTime(endDate);

		List<Visit> visitors = new ArrayList<Visit>();
		boolean nonEmptyAgencies = (agencyIds != null && agencyIds.size() > 0 && agencyIds.get(0) != null);
		boolean nonEmptySeniors = (seniorIds != null && !seniorIds.isEmpty());
		if (nonEmptyAgencies || nonEmptySeniors) {
			if(nonEmptyAgencies && nonEmptySeniors){
				visitors = visitRepository.findAllVisitorsByAgenciesAndSeniors(today.toDate(), endDay.toDate(), agencyIds, seniorIds);
			} else if(nonEmptyAgencies){
				visitors = visitRepository.findAllVisitorsByAgencies(today.toDate(), endDay.toDate(), agencyIds);
			} else if(nonEmptySeniors) {
				visitors = visitRepository.findAllVisitorsBySeniors(today.toDate(), endDay.toDate(), seniorIds);
			}
		} else {
			if (isSuperAdmin(permissionAdmin)) {
				visitors = visitRepository.findAllVisitors(today.toDate(), endDay.toDate());
			} else if (isCompanyAdmin(permissionAdmin)) {
				AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

				if (adminAgencyCompanyOrganization != null) {

					List<Agency> agencies = agencyService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId());
					if (agencies != null && agencies.size() > 0) {
						List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
						visitors = visitRepository.findAllVisitorsByAgencies(today.toDate(), endDay.toDate(), ids);
					}
				}
			} else if (isAgencyAdmin(permissionAdmin)) {
				AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

				if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
					List<Long> ids = new ArrayList<Long>();
					ids.add(adminAgencyCompanyOrganization.getAgency().getId());
					visitors = visitRepository.findAllVisitorsByAgencies(today.toDate(), endDay.toDate(), ids);
				}
			}
		}
		VisitStatusDTO outputVisitors = new VisitStatusDTO();

		if (visitors != null) {
			// convert to map
			LinkedHashMap<Long, ArrayList<Visit>> convertedMap = new LinkedHashMap<Long, ArrayList<Visit>>();

			for (Visit visitor: visitors) {
				Long userKey = visitor.getUser().getId();
				if (convertedMap.containsKey(visitor.getUser().getId())) {
					convertedMap.get(userKey).add(visitor);
				} else {
					ArrayList<Visit> convertedVisit = new ArrayList<Visit>();
					convertedVisit.add(visitor);
					convertedMap.put(userKey, convertedVisit);
				}
			}

			Iterator<Long> keys = convertedMap.keySet().iterator();
			while (keys.hasNext()) {
				Long key = keys.next();
				ArrayList<Visit> followedVisitors = convertedMap.get(key);

				ArrayList<Visit> validedVistors = new ArrayList<Visit>();
				// get visitors are within authorization start and end
				for (Visit visitor: followedVisitors) {
					if (visitor.getUser().getAuthorizationStart() != null &&
							visitor.getUser().getAuthorizationEnd() != null) {
						if (visitor.getCheckInTime().after(visitor.getUser().getAuthorizationStart())
								&& visitor.getCheckInTime().before(visitor.getUser().getAuthorizationEnd())) {
							validedVistors.add(visitor);
						} else {
							// INVALID: unauthorized visits
							outputVisitors.getInValidVisitors().add(new VisitResponseDTO(visitor, VisitTypeEnum.TYPE5.getValue()));
						}
					}
				}

				long previousSumWeeklyHours = 0;
				long previousSumWeeklyDay   = 0;
				for (int i = 0; i < validedVistors.size(); i++) {
					Visit visitor = validedVistors.get(i);
					if(visitor.getStatus().equalsIgnoreCase(VisitStatusEnum.RESERVED.toString())) {
						continue;
					}
					if (visitor.getCheckOutTime() == null) {
						outputVisitors.getInValidVisitors().add(new VisitResponseDTO(visitor, VisitTypeEnum.TYPE2.getValue()));
					} else {
						FixedStatusEnum fixedStatus = visitor.getUser().getFixedStatus();
						// for FIXED TYPE
						if (FixedStatusEnum.FIXED.equals(fixedStatus)) {
							String daysInWeek = visitor.getUser().getDaysInWeek();
							if (daysInWeek != null && daysInWeek.length() > 0) {
								List<String> splittedDays = Arrays.asList(daysInWeek.split(","));

								if (splittedDays.contains(String.valueOf(visitor.getCheckInTime().toLocalDateTime().getDayOfWeek().getValue()))) {
									// VALID: TYPE 1 valid ride
									outputVisitors.getValidVisitors().add(new VisitResponseDTO(visitor, VisitTypeEnum.TYPE1.getValue()));
								} else {
									// INVALID: unauthorized visits
									outputVisitors.getInValidVisitors().add(new VisitResponseDTO(visitor, VisitTypeEnum.TYPE5.getValue()));
								}
							} else {
								// INVALID: unauthorized visits
								outputVisitors.getInValidVisitors().add(new VisitResponseDTO(visitor, VisitTypeEnum.TYPE5.getValue()));
							}
						}
						// for UNFIXED TYPE
						else if(FixedStatusEnum.UNFIXED.equals(fixedStatus)) {
							// hour
							if (UnitTypeEnum.HOUR.equals(visitor.getUser().getUnitType())) {
								if (PeriodTypeEnum.WEEKLY.equals(visitor.getUser().getPeriodType())) {
									// calculate diff hour between check in and check out
									long diff = visitor.getCheckOutTime().getTime() - visitor.getCheckInTime().getTime();
									long diffHours = diff / (60 * 60 * 1000);
									long currentSum = diffHours + previousSumWeeklyHours;
									if (visitor.getUser().getMaximumUnits() == 0 || currentSum <= visitor.getUser().getMaximumUnits()) {
										// TYPE 1: VALID RIDE
										outputVisitors.getValidVisitors().add(new VisitResponseDTO(visitor, VisitTypeEnum.TYPE1.getValue()));
									} else {
										// CHECKING IN CASE NEW CYCLE
										if (diffHours <=  visitor.getUser().getMaximumUnits()) {
											if (visitor.getUser().getAuthWeekStart().getValue() ==
													visitor.getCheckInTime().toLocalDateTime().getDayOfWeek().getValue()) {
												// TYPE 1: VALID RIDE
												outputVisitors.getValidVisitors().add(new VisitResponseDTO(visitor, VisitTypeEnum.TYPE1.getValue()));
											}
										} else {
											// TYPE 4: Invalid visits - Exceed the maximum units
											outputVisitors.getInValidVisitors().add(new VisitResponseDTO(visitor, VisitTypeEnum.TYPE4.getValue()));
										}
									}
									// set back previous hour
									previousSumWeeklyHours = currentSum;
								} else if (PeriodTypeEnum.ENTIRE_AUTH.equals(visitor.getUser().getPeriodType())) {
									// calculate
									long diff = visitor.getCheckOutTime().getTime() - visitor.getUser().getAuthorizationStart().getTime();
									long diffHours = diff / (60 * 60 * 1000);
									if (diffHours <= visitor.getUser().getMaximumUnits()) {
										// TYPE 1: VALID RIDE
										outputVisitors.getValidVisitors().add(new VisitResponseDTO(visitor, VisitTypeEnum.TYPE1.getValue()));
									} else {
										// TYPE 4: Invalid visits - Exceed the maximum units
										outputVisitors.getInValidVisitors().add(new VisitResponseDTO(visitor, VisitTypeEnum.TYPE4.getValue()));
									}
								}

							}
							// day
							else if (UnitTypeEnum.DAY.equals(visitor.getUser().getUnitType())) {
								if (PeriodTypeEnum.WEEKLY.equals(visitor.getUser().getPeriodType())) {
									// calculate diff hour between check in and check out
									long diff = visitor.getCheckOutTime().getTime() - visitor.getCheckInTime().getTime();
									long diffHours = diff / (60 * 60 * 1000);
									if (diffHours < 4) {
										// TYPE 3: INVALID - visit shorter than 4 hours per day
										outputVisitors.getInValidVisitors().add(new VisitResponseDTO(visitor, VisitTypeEnum.TYPE3.getValue()));
									} else {
										previousSumWeeklyDay += 1;
										if (visitor.getUser().getMaximumUnits() == 0 || previousSumWeeklyDay <  visitor.getUser().getMaximumUnits()) {
											// TYPE 1: VALID RIDE
											outputVisitors.getValidVisitors().add(new VisitResponseDTO(visitor, VisitTypeEnum.TYPE1.getValue()));
										} else {
											// CHECKING IN CASE NEW CYCLE
											if (visitor.getUser().getAuthWeekStart().getValue() ==
													visitor.getCheckInTime().toLocalDateTime().getDayOfWeek().getValue()) {
												// reset previousSumWeeklyDay
												previousSumWeeklyDay = 1;
												// TYPE 1: VALID RIDE
												outputVisitors.getValidVisitors().add(new VisitResponseDTO(visitor, VisitTypeEnum.TYPE1.getValue()));
											} else {
												// TYPE 4: Invalid visits - Exceed the maximum units
												outputVisitors.getInValidVisitors().add(new VisitResponseDTO(visitor, VisitTypeEnum.TYPE4.getValue()));
											}
										}
									}
								} else if (PeriodTypeEnum.ENTIRE_AUTH.equals(visitor.getUser().getPeriodType())) {
									// calculate days from
									long diff = visitor.getCheckOutTime().getTime() - visitor.getUser().getAuthorizationStart().getTime();
									long diffDays = diff / (24 * 60 * 60 * 1000);
									if (visitor.getUser().getMaximumUnits() == 0 || diffDays < visitor.getUser().getMaximumUnits()) {
										// TYPE 1: VALID RIDE
										outputVisitors.getValidVisitors().add(new VisitResponseDTO(visitor, VisitTypeEnum.TYPE1.getValue()));
									} else {
										// TYPE 4: Invalid visits - Exceed the maximum units
										outputVisitors.getInValidVisitors().add(new VisitResponseDTO(visitor, VisitTypeEnum.TYPE4.getValue()));
									}
								}
							}
						}
						// not defined
						else {
							outputVisitors.getInValidVisitors().add(new VisitResponseDTO(visitor, VisitTypeEnum.TYPE6.getValue()));
						}
					}
				}
			}
		}

		//logic for billing

		for(VisitResponseDTO valid_visit : outputVisitors.getValidVisitors()){
			Billing billing_info = billingService.findByVisitId(valid_visit.getId());
			if(billing_info != null){
				valid_visit.setBilling(billing_info);
			}	else {
				billing_info = new Billing();
				billing_info.setStatus("");
				valid_visit.setBilling(billing_info);
			}

		}
		return outputVisitors;
	}


	public Map<String,Long> getStatistics(Admin permissionAdmin, Date date) {

		List<Visit> visits = new ArrayList<Visit>();

		if (isSuperAdmin(permissionAdmin)) {
			visits = visitRepository.getStatisticsForToday(date);
		} else if (isCompanyAdmin(permissionAdmin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

			if (adminAgencyCompanyOrganization != null) {

				List<Agency> agencies = agencyService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId());
				if (agencies != null && agencies.size() > 0) {
					List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
					visits = visitRepository.getStatisticsForTodayByAgencyIds(date, ids);
				}
			}
		} else if (isAgencyAdmin(permissionAdmin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				visits = visitRepository.getStatisticsForTodayByAgencyId(date, adminAgencyCompanyOrganization.getAgency().getId());
			}
		}

		long reservedCount = 0;
		long checkedInCount = 0;
		long checkedOutCount = 0;
		long absentCount = 0;
		long comeWithoutAuthCount = 0;
		long stayLessHours = 0;
		long mealOrderCount = 0;

		for(Visit visit : visits) {
			if(visit.getReservedDate() != null) {
				reservedCount ++;
			}
			if("CHECK_IN".equalsIgnoreCase(visit.getStatus())) {
				checkedInCount ++;
			}
			if("CHECK_OUT".equalsIgnoreCase(visit.getStatus())) {
				checkedOutCount ++;
			}
			if(visit.getReservedDate() != null
					&& "RESERVED".equalsIgnoreCase(visit.getStatus())) {
				absentCount ++;
			}
			if(visit.getReservedDate() == null) {
				comeWithoutAuthCount ++;
			}
			if(visit.getCheckOutTime() != null
					&& visit.getCheckInTime() != null
					&& "CHECK_OUT".equalsIgnoreCase(visit.getStatus())) {
				long diff = visit.getCheckOutTime().getTime() - visit.getCheckInTime().getTime();
				long requiredHours = 4;
				if(visit.getAgency() != null){
					requiredHours = visit.getAgency().getRequiredHours();
				}
				long diffHours = diff / (60 * 60 * 1000);
				if (diffHours < requiredHours) {
					stayLessHours ++;
				}
			}
			if(visit.getSelectedBreakfast() != null || visit.getSelectedLunch() != null || visit.getSelectedDinner() != null){
				mealOrderCount++;
			}
		}
		Map<String, Long> map = new HashMap<>();
		map.put("reservedCount", reservedCount);
		map.put("checkedInCount", checkedInCount);
		map.put("checkedOutCount", checkedOutCount);
		map.put("absentCount", absentCount);
		map.put("comeWithoutAuthCount", comeWithoutAuthCount);
		map.put("stayLessHours", stayLessHours);
		map.put("mealOrderCount", mealOrderCount);
		return map;
	}

	public Page<Visit> findAllVisitorByDate(String startDate, String endDate, Pageable pageable, List<String> status, String statFilter, Admin permissionAdmin) {

		boolean isSuperAdmin = true;
		List<Long> ids = new ArrayList<>();
		if (isCompanyAdmin(permissionAdmin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

			if (adminAgencyCompanyOrganization != null) {

				List<Agency> agencies = agencyService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId());
				if (agencies != null && agencies.size() > 0) {
					ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
				}
				isSuperAdmin = false;
			}
		} else if (isAgencyAdmin(permissionAdmin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				ids = Arrays.asList(adminAgencyCompanyOrganization.getAgency().getId());
			}
			isSuperAdmin = false;
		} else {
			ids = Arrays.asList(new Long(1));
		}


		String checkIn = "CHECK_IN";
		String checkOut = "CHECK_OUT";
		String reserved = "RESERVED";
		if(!CollectionUtils.isEmpty(status)) {
			if(!status.contains(checkIn)) {
				checkIn = "dummy";
			}
			if(!status.contains(checkOut)) {
				checkOut = "dummy";
			}
			if(!status.contains(reserved)) {
				reserved = "dummy";
			}
		}
		boolean isReservedFilter = false;
		boolean isAbsentFilter = false;
		boolean isComeWithoutAuthFilter = false;
		boolean isStayLessHoursFilter = false;
		Long requiredHours = new Long(4);
		if(!StringUtils.isEmpty(statFilter)) {
			checkIn = "dummy";
			checkOut = "dummy";
			reserved = "dummy";
			if (statFilter.equalsIgnoreCase("reserved")) {
				isReservedFilter = true;
				checkIn = "CHECK_IN";
				checkOut = "CHECK_OUT";
				reserved = "RESERVED";
			} else if (statFilter.equalsIgnoreCase("checkedIn")) {
				checkIn = "CHECK_IN";
			} else if (statFilter.equalsIgnoreCase("checkedOut")) {
				checkOut = "CHECK_OUT";
			} else if (statFilter.equalsIgnoreCase("absent")) {
				reserved = "RESERVED";
				isAbsentFilter = true;
			} else if (statFilter.equalsIgnoreCase("comeWithoutAuth")) {
				checkIn = "CHECK_IN";
				checkOut = "CHECK_OUT";
				isComeWithoutAuthFilter = true;
			} else if (statFilter.equalsIgnoreCase("stayLessHours")) {
				checkOut = "CHECK_OUT";
				isStayLessHoursFilter = true;
				if(permissionAdmin.getAgency() != null){
					requiredHours = permissionAdmin.getAgency().getRequiredHours();
				}
			}
		}
		DateTime today = new DateTime(startDate);
		DateTime endDay = new DateTime(endDate);
		pageable = checkPageable(pageable);
		return visitRepository.findAllVisitorByTime(today.toDate(), endDay.toDate(), pageable, checkIn, checkOut, reserved, isReservedFilter, isAbsentFilter, isComeWithoutAuthFilter, isStayLessHoursFilter, isSuperAdmin, ids, requiredHours);
	}

	@Override
	public MobileCheckinRequestDTO mobileCheckIn(MobileCheckinRequestDTO mobileCheckinRequestDTO, Visit visit) {
		User user = null;

		if (mobileCheckinRequestDTO.getUserId() != null) {
			user = userService.findById(mobileCheckinRequestDTO.getUserId().longValue());
			visit.setUser(user);
		}

		if (mobileCheckinRequestDTO.getAgencyId() != null) {
			visit.setAgency(agencyService.findById(mobileCheckinRequestDTO.getAgencyId().longValue()));
		}


		if (mobileCheckinRequestDTO.getSeatId() != null) {
			visit.setSeat(seatService.findById(mobileCheckinRequestDTO.getSeatId().longValue()));
			visit.getSeat().setUser(user);
			if (mobileCheckinRequestDTO.getTableId() != null) {
				visit.getSeat().setTable(tableService.findById(mobileCheckinRequestDTO.getTableId().longValue()));
			}
			seatService.save(visit.getSeat());
		}

		visit.setCheckInTime(new Timestamp(new Date().getTime()));
		visit.setStatus(VisitStatusEnum.CHECK_IN.toString());

		visit.setSelectedBreakfast(mealService.findById(mobileCheckinRequestDTO.getBreakfastMealId().longValue()));
		visit.setSelectedDinner(mealService.findById(mobileCheckinRequestDTO.getDinnerMealId().longValue()));
		visit.setSelectedLunch(mealService.findById(mobileCheckinRequestDTO.getLunchMealId().longValue()));
		visit.setActivityId(mobileCheckinRequestDTO.getActivityId().longValue());


		visit.setSignature(mobileCheckinRequestDTO.getSignature());
		save(visit);
		return mobileCheckinRequestDTO;
	}


	@Override
	public ExpressVisitRequestDTO expressCheckin(ExpressVisitRequestDTO expressVisitRequestDTO, Visit visit) {
		User user = null;

		DateTime today = new DateTime();
		List<Visit> list = visitRepository.findByPin(visit.getPin(), today.toDate());
		Visit visitDB = ((list != null && list.size() > 0) ? list.get(0) : null);

		if(visitDB != null
				&& (VisitStatusEnum.RESERVED.toString().equalsIgnoreCase(visitDB.getStatus())
				|| VisitStatusEnum.CHECK_IN.toString().equalsIgnoreCase(visitDB.getStatus()))) {
			visit = visitDB;
		}

		if(expressVisitRequestDTO.getUserId() != null){
			user = userService.findById(expressVisitRequestDTO.getUserId().longValue());
			visit.setUser(user);
		}

		if(expressVisitRequestDTO.getAgencyId() != null){
			visit.setAgency(agencyService.findById(expressVisitRequestDTO.getAgencyId().longValue()));
		}

		visit.setCheckInTime(new Timestamp(new Date().getTime()));
		visit.setStatus(VisitStatusEnum.CHECK_IN.toString());
		visit.setSignature(expressVisitRequestDTO.getSignature());

		save(visit);
		expressVisitRequestDTO.setId(visit.getId().intValue());

		return expressVisitRequestDTO;
	}

	@Override
	public ExpressVisitRequestDTO expressCheckOut(ExpressVisitRequestDTO expressVisitRequestDTO) {
		Visit visit = null;

		long visitId;
		try{
			visitId = expressVisitRequestDTO.getId().longValue();
			}catch(ApplicationException e){
				throw new ApplicationException(Response.ResultCode.VISIT_NOT_FOUND,
						"Visit not found, Please provide visit id.");
			}

		visit = visitRepository.findByByBinAndId(expressVisitRequestDTO.getPin(), visitId);

		// if no visit found
		if (visit == null) {
			throw new ApplicationException(Response.ResultCode.VISIT_NOT_FOUND,
					"Visit not found, Please provide visit id.");
		}

		// check out
		if (VisitStatusEnum.CHECK_IN.equals(VisitStatusEnum.valueOf(visit.getStatus()))) {
			visit.setCheckOutTime(new Timestamp(new Date().getTime()));
			visit.setStatus(VisitStatusEnum.CHECK_OUT.toString());
			visit.setSignature(expressVisitRequestDTO.getSignature());
			save(visit);
		} else {
			throw new ApplicationException(Response.ResultCode.INVALID_STATUS, "Invalid visit status");
		}

		return expressVisitRequestDTO;
  }


  public Page<Visit> findAllByAgency(Long agencyId, Pageable pageable) {
		return visitRepository.findAllByAgencyId(agencyId, pageable);
	}

  @Override
  public Meal updateVisitMealSummary(Meal meal) {
    Meal actualMeal = mealService.findById(meal.getId());
    actualMeal.setMealStatus(meal.getMealStatus());
    return mealService.save(actualMeal);
  }

  @Override
	public List<Visit> findByAgencyUser(Long agencyId, Long userId) {
	  DateTime today = new DateTime();
	  DateTime startDate = today.withTimeAtStartOfDay();
	  DateTime endDate = startDate.plusDays(1);
	  return visitRepository.findByAgencyUser(agencyId, userId, startDate.toDate(), endDate.toDate());
  }

  @Override
  public List<Visit> findAllOnlyCheckedInVisitCount(Long agencyId, Date startDate, Date endDate) {
	  return visitRepository.findAllOnlyCheckedInVisit(agencyId, startDate, endDate);
  }

  @Override
  public void doAutoCheckoutTimeByToday() {
		// find checkout time setting
	    // 0. agency, 1. auto_checkout_time
	    List<Object[]> settingFollowingAgencies = adminSettingRepository.findAllSettingGroupAgency();
	    // map to key value (key is agency id, value auto_checkout_time
	    Map<Long, String> autoSettingCheckoutMap = new HashMap<Long, String>();
	    if (settingFollowingAgencies != null && settingFollowingAgencies.size() > 0) {
	    	for (Object[] ob: settingFollowingAgencies) {
	    		Integer agencyId = (Integer) ob[0];
	    		String autoTime = (String) ob[1];
	    		autoSettingCheckoutMap.put(agencyId.longValue(), autoTime);
			}
		}

	    // get checkout time missing from yesterday
	    DateTime endDate = new DateTime();
	    DateTime startDate = endDate.minusDays(1).withTimeAtStartOfDay();

	    // only get by today
	    List<Visit> forgotCheckouts = visitRepository.findAllOnlyCheckedInVisit(startDate.toDate(), startDate.toDate());

	    if (forgotCheckouts != null && forgotCheckouts.size() > 0) {
	    	for (Visit visit: forgotCheckouts) {
	    		// the auto checkout time is set
				// default time
				DateTime actualCheckoutTime = startDate.withTime(22, 0, 0, 0);
	    		if (autoSettingCheckoutMap.containsKey(visit.getAgency().getId())) {
					String[] timeSplited = autoSettingCheckoutMap.get(visit.getAgency().getId()).split(":");
					// get exactly date
					actualCheckoutTime = startDate.withTime(Integer.valueOf(timeSplited[0]), Integer.valueOf(timeSplited[1]), 0, 0);
				}

				visit.setStatus(VisitStatusEnum.CHECK_OUT.toString());
				visit.setCheckOutTime(new Timestamp(actualCheckoutTime.toDate().getTime()));

				// reset seat for that visit
				Seat seat = visit.getSeat();
				if(seat!=null)
				{
					seat.setUser(null);
					seat.setStatus(SeatStatusEnum.AVAILABLE);
					seatService.save(seat);
				}
				save(visit);
			}
		}


	    /*String autoCheckoutTime = adminSettingRepository.findMaxAutoCheckoutTime();

	    if (autoCheckoutTime != null && !autoCheckoutTime.isEmpty()) {
	    	Date autoDate = DateUtils.stringToDate("HH:mm", autoCheckoutTime);
	    	if (autoDate != null) {
	    		DateTime autoDateTime = new DateTime(autoDate);
				DateTime today = new DateTime();
				DateTime startDate = today.withTimeAtStartOfDay();
				DateTime endDate = startDate.plusDays(1);
				if (autoDateTime.getHourOfDay() == today.getHourOfDay()) {
					List<Visit> forgotCheckouts = visitRepository.findAllOnlyCheckedInVisit(startDate.toDate(), endDate.toDate());
					if (forgotCheckouts != null && forgotCheckouts.size() > 0) {
						for (Visit visit: forgotCheckouts) {
							visit.setStatus(VisitStatusEnum.CHECK_OUT.toString());
							visit.setCheckOutTime(new Timestamp(autoDate.getTime()));
							visitRepository.save(visit);
						}
					}
				}
			}
		}*/
  }

  @Override
  public List<Visit> findAllReservedByToday(Long agencyId) {
		DateTime today = new DateTime();
		DateTime startDate = today.withTimeAtStartOfDay();
		DateTime endDate = startDate.plusDays(1);

		return visitRepository.findAllReservedByToday(agencyId, startDate.toDate(), endDate.toDate());
  }

	public Visit findByStatusByToday(Long agencyId, String pin, String status) {
		DateTime today = new DateTime();
		DateTime startDate = today.withTimeAtStartOfDay();
		DateTime endDate = startDate.plusDays(1);
		return visitRepository.findOneByStatusByToday(agencyId, pin, startDate.toDate(), endDate.toDate(), status);
	}

	@Override
	@Transactional
	public byte[] generateAndDownloadMealOrderReport(Admin permissionAdmin, String startDate) {
		DateTime currentTime = new DateTime(startDate);
		DateTime endTime   = currentTime.plusHours(24);
		List<Object[]> result = new ArrayList<Object[]>();
		if (isSuperAdmin(permissionAdmin)) {
			result = visitRepository.findMealOrderReport(currentTime.toDate(), endTime.toDate());
		} else if (isCompanyAdmin(permissionAdmin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());
			if (adminAgencyCompanyOrganization != null) {
				List<Agency> agencies = agencyService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId());
				if (agencies != null && agencies.size() > 0) {
					List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
					result = visitRepository.findMealOrderReportByCompany(currentTime.toDate(), endTime.toDate(), ids);
				}
			}
		} else if (isAgencyAdmin(permissionAdmin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());
			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				result = visitRepository.findMealOrderReportByAgency(currentTime.toDate(), endTime.toDate(), adminAgencyCompanyOrganization.getAgency().getId());
			}
		}
		try {
			File file = reportService.generateMealOrderReport(result, currentTime.toDate());
			return IOUtils.toByteArray(new FileInputStream(file));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public Visit createVisit(Visit visit) {
		String pin = getDigitPin(visit.getPin());
		User user = getUser(pin, visit.getAgency());
		Document checkInPhoto = visit.getCheckinPhoto();
		Visit lastC = findByStatusByToday(user.getAgency().getId(), user.getPin(), VisitStatusEnum.CHECK_IN.name());
		if(lastC != null) {
			lastC.setSeat(getSeat(lastC));
			lastC.setActivityId(visit.getActivityId());
			lastC.setSelectedBreakfast(visit.getSelectedBreakfast());
			lastC.setSelectedLunch(visit.getSelectedLunch());
			lastC.setSelectedDinner(visit.getSelectedDinner());
			// store to database
			lastC = save(lastC);
			visit = lastC;
			if (lastC.getSeat() != null) {
				lastC.getSeat().setStatus(SeatStatusEnum.TAKEN);
				lastC.getSeat().setUser(user);
				seatService.save(lastC.getSeat());
			}
			return visit;
		}
		Visit lastR = findByStatusByToday(user.getAgency().getId(), user.getPin(), VisitStatusEnum.RESERVED.name());
		if(lastR != null) {
			lastR.setCheckInTime(new Timestamp(new Date().getTime()));
			lastR.setStatus(VisitStatusEnum.CHECK_IN.toString());
			lastR.setSeat(getSeat(lastR));
			lastR.setActivityId(visit.getActivityId());
			lastR.setSelectedBreakfast(visit.getSelectedBreakfast());
			lastR.setSelectedLunch(visit.getSelectedLunch());
			lastR.setSelectedDinner(visit.getSelectedDinner());
			if (checkInPhoto != null) {
				lastR.setCheckinPhoto(checkInPhoto);
			}
			// store to database
			lastR = save(lastR);
			visit = lastR;
			if (lastR.getSeat() != null) {
				lastR.getSeat().setStatus(SeatStatusEnum.TAKEN);
				lastR.getSeat().setUser(user);
				seatService.save(lastR.getSeat());
			}
			return visit;
		}
		visit.setUser(user);
		visit.setStatus(VisitStatusEnum.CHECK_IN.toString());
		visit.setSeat(getSeat(visit));
		visit = save(visit);
		if (visit.getSeat() != null) {
			visit.getSeat().setStatus(SeatStatusEnum.TAKEN);
			visit.getSeat().setUser(user);
			seatService.save(visit.getSeat());
		}
		return visit;
	}

	private String getDigitPin(String pin) {
		if (pin == null || pin.isEmpty())
			return null;
		// This is to handle online pinOrBarcode which in format PIN-Firstname lastname
		if (pin.contains("-")) {
			return pin.substring(0, pin.indexOf("-"));
		}
		return pin;
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

	private Seat getSeat(Visit visit) {
		if (visit.getSeat() == null || visit.getSeat().getId() == null)
			return null;
		return seatService.findById(visit.getSeat().getId());
	}
}
