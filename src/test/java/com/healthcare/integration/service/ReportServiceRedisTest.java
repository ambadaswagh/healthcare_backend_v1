package com.healthcare.integration.service;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Report;
import com.healthcare.repository.ReportRepository;
import com.healthcare.service.ReportService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ReportServiceRedisTest {
	@Autowired
	private ReportService reportService;

	@MockBean
	private ReportRepository reportRepository;


	
	String imgUrl = "/img/a.jpg";
	Calendar createdAt = Calendar.getInstance();
	Integer displayOrder = 1;
	long baseId = 1;
	String dataColumns = "Data columns";
	Calendar endDate = Calendar.getInstance();
	String format = "Report format";
	String reportTitle = "Report title";
	Calendar startDate = Calendar.getInstance();

	private Admin admin;
	private Report report;
	private Long id = 7L;
	
	@Autowired
	private TestEntityServiceFactory testEntitySericeFactory;
	
	@Before
	public void setup() {
		testEntitySericeFactory.saveData = true;
		admin  = testEntitySericeFactory.createAdmin();
    	report = null;
	}

	@After
	public void rollback() {
		if(report!=null){
			reportService.deleteById(report.getId());
		}
		testEntitySericeFactory.redisCleanUp();
	}


	@Test
	public void testSaveReport() {
		report = createNewReport();
		report.setId(id);
		Mockito.when(reportRepository.save(report)).thenReturn(report);
		reportService.save(report);
		Report savedReport = reportService.findById(report.getId());
		Assert.assertNotNull(savedReport);
	}

	@Test
	public void testUpdateReport() {
		String newReportTitle = "New Report Title";

		report = createNewReport();
		report.setId(id);
		Mockito.when(reportRepository.save(report)).thenReturn(report);
		reportService.save(report);
		Report reportSaved = reportService.findById(report.getId());
		reportSaved.setReportTitle(newReportTitle);
		Mockito.when(reportRepository.save(reportSaved)).thenReturn(reportSaved);
		reportService.save(reportSaved);
		Report reportMofified = reportService.findById(report.getId());
		Assert.assertEquals(reportMofified.getReportTitle(), newReportTitle);
	}

	@Test
	public void testDeleteReport() {
		Report report = createNewReport();
		report.setId(id);
		Mockito.when(reportRepository.save(report)).thenReturn(report);
		reportService.save(report);
		Mockito.doNothing().when(reportRepository).delete(report.getId());
		Assert.assertNotNull(reportService.deleteById(report.getId()));
	}

	private Report createNewReport() {
		Report report = new Report();
		report.setAdmin(admin);
		report.setBaseId(baseId);
		//report.setCompany(admin.getRole().getAgency().getCompany());
		report.setDataColumns(dataColumns);
		report.setEndDate(new Timestamp(endDate.getTimeInMillis()));
		report.setFormat(format);
		report.setReportTitle(reportTitle);
		report.setStartDate(new Timestamp(startDate.getTimeInMillis()));
		report.setCreatedAt(new Timestamp(createdAt.getTimeInMillis()));
		report.setRole(admin.getRole());
		return report;
	}
	
}
