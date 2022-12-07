package com.healthcare.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import com.healthcare.dto.RideValidInvalidDTO;
import com.healthcare.dto.VisitReportContext;
import com.healthcare.dto.VisitStatusDTO;
import com.healthcare.model.entity.Report;
import com.itextpdf.text.DocumentException;

import javax.servlet.http.HttpServletRequest;

public interface ReportService extends IService<Report>, IFinder<Report> {
	List<Report> findAll();

	public File generateVisitReport(VisitReportContext context) throws FileNotFoundException, DocumentException;

	File generateVisitBillingReport(VisitStatusDTO visitStatusDtos, int isValidVisitor, HttpServletRequest request);

	File generateRideBillingReport(RideValidInvalidDTO rideValidInvalidDTO, int isValidVisitor, HttpServletRequest request);

	File generateMealOrderReport(List<Object[]> results, Date startDate) throws FileNotFoundException, DocumentException;
}
