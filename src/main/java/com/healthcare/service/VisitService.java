package com.healthcare.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.healthcare.dto.*;
import com.healthcare.model.entity.Meal;

import com.healthcare.model.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.healthcare.api.model.ExpressVisitRequestDTO;
import com.healthcare.api.model.MobileCheckinRequestDTO;
import com.healthcare.api.model.VisitReportRequest;
import com.healthcare.api.model.VisitReportResponse;
import com.healthcare.api.model.VisitRequest;
import com.healthcare.api.model.VisitSummary;
import com.healthcare.model.entity.Visit;

import javax.servlet.http.HttpServletRequest;

public interface VisitService extends IService<Visit>, IFinder<Visit> {
	List<Visit> findAllByServicePlanId(Long servicePlanId);

	Visit checkIn(VisitRequest visit);

	MobileCheckinRequestDTO mobileCheckIn(MobileCheckinRequestDTO mobileCheckinRequestDTO, Visit visit);

	ExpressVisitRequestDTO expressCheckin(ExpressVisitRequestDTO expressVisitRequestDTO, Visit visit);

	ExpressVisitRequestDTO expressCheckOut(ExpressVisitRequestDTO expressVisitRequestDTO);

	Visit checkOut(VisitRequest visit);

	List<Visit> findAll();

	Page<Visit> findAll(Pageable pageable, List<String> status);

	List<VisitReportResponse> getAllVisitReport(VisitReportRequest visitReportRequest);


	List<VisitSummary> getAllVisitSummary(Admin admin, String startDate);

	public List<VisitDTO> getVisitReport(BasicReportFilterDTO req);

	public Page<Visit> getVisitReport(BasicReportFilterDTO req, Pageable pageable);
	public byte[] getVisitReportAndDownload(BasicReportFilterDTO req);

	byte[] getVisitBillingReportAndDownload(VisitRequestDTO reg, HttpServletRequest request);

	public VisitSeniorStatsResDTO getVisitReportSeniorsStats(VisitSeniorStatsReqDTO req);

	Visit billVisit(String billingCode, Long visiId);

	List<Visit> findAllByAgencyId(Long agencyId);

	VisitStatusDTO getAuthorizedVisitors(Admin admin, String startDate, String endDate, List<Long> agencyIds, List<Long> seniorIds, Pageable pageable, boolean isInvalid);

	Page<Visit> findAllVisitorByDate(String startDate, String endDate, Pageable pageable, List<String> status, String statFilter, Admin permissionAdmin);


	Meal updateVisitMealSummary(Meal meal);

	Map<String,Long> getStatistics(Admin permissionAdmin, Date date);
	Page<Visit> findAllByAgency(Long agencyId, Pageable pageable);

	List<Visit> findByAgencyUser(Long agencyId, Long userId);

	List<Visit> findAllOnlyCheckedInVisitCount(Long agencyId, Date startDate, Date endDate);

	void doAutoCheckoutTimeByToday();

	List<Visit> findAllReservedByToday(Long agencyId);

	byte[] generateAndDownloadMealOrderReport(Admin permissionAdmin, String startDate);

	Visit createVisit(Visit visit);
}
