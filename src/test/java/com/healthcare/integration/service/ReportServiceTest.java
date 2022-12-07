package com.healthcare.integration.service;

import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Report;
import com.healthcare.model.entity.Report;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.ReportService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ReportServiceTest {
	@Autowired
	private ReportService reportService;

	@Value("${spring.data.rest.default-page-size}")
	private int defaultMaxSize;

	Calendar createdAt = Calendar.getInstance();
	Integer displayOrder = 1;
	long baseId = 1;
	String dataColumns = "Data columns";
	Calendar endDate = Calendar.getInstance();
	String format = "Report format";
	String reportTitle = "Report title";
	Calendar startDate = Calendar.getInstance();

	Admin admin;
	private Report report;
	
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
		reportService.save(report);
		Assert.assertNotNull(report.getId());
	}

	@Test
	public void testGetReport() {
		report = createNewReport();
		reportService.save(report);
		Assert.assertNotNull(reportService.findById(report.getId()));
	}

	@Test
	public void testUpdateReport() {
		String newReportTitle = "New Report Title";

		report = createNewReport();
		reportService.save(report);
		Assert.assertEquals(report.getReportTitle(), reportTitle);
		Report reportSaved = reportService.findById(report.getId());
		reportSaved.setReportTitle(newReportTitle);
		reportService.save(reportSaved);
		Report reportMofified = reportService.findById(report.getId());
		Assert.assertEquals(reportMofified.getReportTitle(), newReportTitle);
	}

	@Test
	public void testDeleteReport() {
		Report report = createNewReport();
		reportService.save(report);
		Assert.assertNotNull(report.getId());
		reportService.deleteById(report.getId());
		Assert.assertNull(reportService.findById(report.getId()));
	}

	private Report createNewReport() {
		Report report = new Report();
		report.setAdmin(admin);
		report.setBaseId(baseId);
		//report.setCompany(admin.getId());
		report.setDataColumns(dataColumns);
		report.setEndDate(new Timestamp(endDate.getTimeInMillis()));
		report.setFormat(format);
		report.setReportTitle(reportTitle);
		report.setStartDate(new Timestamp(startDate.getTimeInMillis()));
		report.setCreatedAt(new Timestamp(createdAt.getTimeInMillis()));
		report.setRole(admin.getRole());
		return report;
	}
	
	
	@Test
	public void testFindAllPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			Report obj = reportService.save(createNewReport());
			listIds.add(obj.getId());
		}

		// PAGE 0
		// given
		Pageable pageable = new CustomPageRequest();
		// when
		Page<Report> result = reportService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == defaultMaxSize);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 0);

		// PAGE 2
		pageable = new PageRequest(1, 5);
		// when
		result = reportService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 1);

		// delete created data
		for (Long id : listIds) {
			reportService.deleteById(id);
		}
	}

	@Test
	public void testSearchWithPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			Report obj = reportService.save(createNewReport());
			listIds.add(obj.getId());
		}
		// SEARCH 1 : FIND BY ID
		// given
		Pageable pageable = new PageRequest(0, 25);
		Report obj = new Report();
		obj.setId(listIds.get(0));
		obj.setBaseId(baseId);
		obj.setCreatedAt(null);
		obj.setDownloadAt(null);

		// when
		Page<Report> result = reportService.findAll(obj, pageable);
		// then
		assertTrue(result.getContent().size() == 1);
		assertTrue(result.getTotalElements() >= 1);
		assertTrue(result.getTotalPages() >= 1);
		assertTrue(result.getNumber() == 0);

		// SEARCH 2
		// given
		pageable = new PageRequest(3, 5);
		obj = new Report();
		obj.setBaseId(baseId);
		obj.setCreatedAt(null);
		obj.setDownloadAt(null);

		// when
		result = reportService.findAll(obj, pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 3);

		// Delete created data
		for (Long id : listIds) {
			reportService.deleteById(id);
		}
	}

}
