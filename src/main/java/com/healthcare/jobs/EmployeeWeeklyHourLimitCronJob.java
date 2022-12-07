package com.healthcare.jobs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.CronJobReport;
import com.healthcare.model.entity.Employee;
import com.healthcare.service.AdminRelationService;
import com.healthcare.service.CronJobReportService;
import com.healthcare.service.EmailSenderService;
import com.healthcare.service.EmployeeService;
import com.healthcare.service.SmsSenderService;

@Service
public class EmployeeWeeklyHourLimitCronJob {

	@Value("${cronjob.enabled}")
	private String conjobEnable;

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private SmsSenderService smsService;
	@Autowired
	private CronJobReportService cronJobReportService;
	@Autowired
	private AdminRelationService adminRelationService;
	@Autowired
	private EmailSenderService emailSender;

	private static String workingLimitMessageTemplate = "${fullname} has worked ${worked} hours this week, his/her limitation is ${limitation} hours per week. Thanks";

	@Scheduled(cron = "${cronjob.empweeklylimitschedule}")
	public void run() {
		if (!Boolean.valueOf(conjobEnable)){
			return;
		}

		List<Employee> employees = employeeService.findEmployeesCrossingWorkLimit(8);
		for (Employee emp : employees) {
			CronJobReport cjr = new CronJobReport();
			cjr.setEmployee(emp);
			cjr.setMessage(getMessageContent(emp, emp.getWeeklyHoursWorked(), emp.getWeeklyWorkingTimeLimitation()));
			StringBuffer error = new StringBuffer();

			try {
				sendEmail(emp, cjr.getMessage());
			} catch (Exception e) {
				error.append("Email send Failed : " + e.getMessage());
			}

			try {
				sendMessage(emp, cjr.getMessage());
			} catch (Exception e) {
				error.append("Message send Failed : " + e.getMessage());
			}

			if (error.length() > 0) {
				cjr.setJobStatus("FAIL");
				cjr.setErrorDesc(error.toString());
			} else {
				cjr.setJobStatus("PASS");
			}

			cronJobReportService.save(cjr);
		}
	}

	private String getMessageContent(Employee employee, String worked, Integer limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fullname", employee.getFirstName() + " " + employee.getLastName());
		map.put("worked", worked);
		map.put("limitation", limit);
		StrSubstitutor sub = new StrSubstitutor(map);
		return sub.replace(this.workingLimitMessageTemplate);
	}

	private void sendEmail(Employee emp, String content) {
		if (emp.getAgency() != null) {
			List<Admin> admins = adminRelationService.getByAgencyId(emp.getAgency().getId());
			admins.forEach(admin -> {
				if (admin.getEmail() != null) {
					String subject = emp.getFirstName() + " " + emp.getLastName() + " working status";
					try {
						emailSender.sendEmail(admin.getEmail(), subject, content, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	private void sendMessage(Employee emp, String body) {
		if (emp.getAgency() != null) {
			List<Admin> admins = adminRelationService.getByAgencyId(emp.getAgency().getId());
			admins.forEach(admin -> {
				if (admin.getPhone() != null) {
					smsService.sendSms(admin.getPhone(), body);
				}
			});
		}
	}

}
