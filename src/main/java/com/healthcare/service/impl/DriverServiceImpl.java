package com.healthcare.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.healthcare.model.entity.*;
import com.healthcare.repository.AdminAgencyCompanyOrganizationRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.repository.DriverRepository;
import com.healthcare.service.DriverService;

import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

@Service
@Transactional
public class DriverServiceImpl extends BasicService<Driver, DriverRepository> implements DriverService {
	private static final String KEY = Driver.class.getSimpleName();

	@Autowired
	DriverRepository driverRepository;

	@Autowired
	private AdminAgencyCompanyOrganizationRepository adminAgencyCompanyOrganizationRepository;

	@Autowired
	private RedisTemplate<String, Driver> driverRedisTemplate;

	@Override
	@Transactional
	public Driver save(Driver driver) {
		if(driver.getId() != null){
			driver.setUpdatedAt(new Timestamp(new Date().getTime()));
	  	} else {
	  		driver.setCreatedAt(new Timestamp(new Date().getTime()));
	  		driver.setUpdatedAt(new Timestamp(new Date().getTime()));
	  	}
		if(driver.getDriverLicenseStart() != null){
			driver.getDriverLicenseStart().setHours(12);
			driver.getDriverLicenseStart().setMinutes(0);
			driver.getDriverLicenseStart().setSeconds(0);
    	}
		if(driver.getDriverLicenseExpire() != null){
			driver.getDriverLicenseExpire().setHours(12);
			driver.getDriverLicenseExpire().setMinutes(0);
			driver.getDriverLicenseExpire().setSeconds(0);
    	}
		if(driver.getDriverTlcFhvLicenseExpire() != null){
			driver.getDriverTlcFhvLicenseExpire().setHours(12);
			driver.getDriverTlcFhvLicenseExpire().setMinutes(0);
			driver.getDriverTlcFhvLicenseExpire().setSeconds(0);
    	}
		if(driver.getDriverTlcFhvLicenseStart() != null){
			driver.getDriverTlcFhvLicenseStart().setHours(12);
			driver.getDriverTlcFhvLicenseStart().setMinutes(0);
			driver.getDriverTlcFhvLicenseStart().setSeconds(0);
    	}
		if(driver.getBackgroundCheckStart() != null){
			driver.getBackgroundCheckStart().setHours(12);
			driver.getBackgroundCheckStart().setMinutes(0);
			driver.getBackgroundCheckStart().setSeconds(0);
    	}
		if(driver.getBackgroundCheckExpire() != null){
			driver.getBackgroundCheckExpire().setHours(12);
			driver.getBackgroundCheckExpire().setMinutes(0);
			driver.getBackgroundCheckExpire().setSeconds(0);
    	}
		if(driver.getDrugScreenStart() != null){
			driver.getDrugScreenStart().setHours(12);
			driver.getDrugScreenStart().setMinutes(0);
			driver.getDrugScreenStart().setSeconds(0);
    	}
		if(driver.getDrugScreenExpire() != null){
			driver.getDrugScreenExpire().setHours(12);
			driver.getDrugScreenExpire().setMinutes(0);
			driver.getDrugScreenExpire().setSeconds(0);
    	}
		if(driver.getDrivingRecordStart() != null){
			driver.getDrivingRecordStart().setHours(12);
			driver.getDrivingRecordStart().setMinutes(0);
			driver.getDrivingRecordStart().setSeconds(0);
    	}
		if(driver.getDrivingRecordExpire() != null){
			driver.getDrivingRecordExpire().setHours(12);
			driver.getDrivingRecordExpire().setMinutes(0);
			driver.getDrivingRecordExpire().setSeconds(0);
    	}
		if(driver.getDob() != null){
			driver.getDob().setHours(12);
			driver.getDob().setMinutes(0);
			driver.getDob().setSeconds(0);
    	}
		driver = driverRepository.save(driver);
		driverRedisTemplate.opsForHash().put(KEY, driver.getId(), driver);
		return driver;
	}

	@Override
	public Driver findById(Long id) {
		Driver driver = driverRepository.findOne(id);
		return driver;
	}

	@Override
	public Long deleteById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long disableById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public List<Driver> findAll() {
		List<Driver> driverList = driverRepository.findAll();
		return driverList;
	}

	/*@Override
	@Transactional
	public Employee findById(Long id) {
		Employee employee = employeeRepository.findOne(id);
		return employee;
	}

	@Override
	@Transactional
	public List<EmployeeClocking> getTotalWorkingTimeThisWeek(Long id) {
		List<Object[]> data = employeeClockingRepository.getWorkingByEmployee(DateUtils.getFirstDayOfCurrentWeek(),
				DateUtils.getLastDayOfCurrentWeek(), id);
		List<EmployeeClocking> listClocking = new ArrayList<EmployeeClocking>();
		data.forEach(object -> {
			listClocking.add(new EmployeeClocking().fromObject(object));
		});
		return listClocking;
	}

	@Override
	@Transactional
	public Long deleteById(Long id) {
		employeeLeaveService.deleteById(id);
		employeeRepository.delete(id);
		return employeeRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override
	@Transactional
	public List<Employee> findAll() {
		List<Employee> employeeList = employeeRepository.findAll();
		return employeeList;
	}

	@Override
	@Transactional
	public List<Employee> findByCampanyIdAndAgencyId(Long companyId, Long agencyId) {
		List<Employee> employeeList = employeeRepository.findByCompany(companyId, agencyId);
		return employeeList;
	}

	@Override
	@Transactional
	public Long disableById(Long id) {
		Employee employee = null;
		if (employeeRedisTemplate.opsForHash().hasKey(KEY, id))
			employee = (Employee) employeeRedisTemplate.opsForHash().get(KEY, id);
		else
			employee = employeeRepository.findById(id);
		if (employee != null && employee.getId() != null) {
			employee.setStatus(EntityStatusEnum.DISABLE.getValue());
			employee = employeeRepository.save(employee);
			employeeRedisTemplate.opsForHash().put(KEY, employee.getId(), employee);
			return employee.getId();
		}
		return null;
	}

	@Override
	public List<Employee> findEmployeesCrossingWorkLimit(int hoursDifference) {
		return employeeRepository.findEmployeesCrossingWorkLimit(DateUtils.getFirstDayOfCurrentWeek(),
				DateUtils.getLastDayOfCurrentWeek(), hoursDifference);
	}

    @Override
    @Transactional
    public Employee savePunch(Employee dbEmp) {
    	System.out.println("punch status is:"+dbEmp.getPunchStatus());
        EmployeeClocking employeeClocking = employeeClockingRepository.getLastClockingByEmployee(dbEmp.getId());
        if(employeeClocking==null || employeeClocking.getCheckOutTime()!=null) {
        employeeClocking = new EmployeeClocking();
        employeeClocking.setCheckInSignatureId(dbEmp.getRulesAndRegusDocId());
        employeeClocking.setCheckInTime(new Timestamp(new Date().getTime()));
        employeeClocking.setEmployee(dbEmp);
        }else{
        	employeeClocking.setCheckOutTime(new Timestamp(new Date().getTime()));
        }
        employeeClockingRepository.save(employeeClocking);
        //dbEmp.setWeeklyHoursWorked("0h0m");
        return save(dbEmp);
    }

	@Override
	public boolean validPin(String pin) {
		boolean isValid = false;
		Employee emp = employeeRepository.findByPin(pin);
		if(emp == null){
			isValid = true;  // valid pin - do not exist in db
		}else{
			isValid = false; // invalid pin - exists in db
		}
		return isValid;
	}

	@Override
	public String generatePin() {
		Random rand = new Random();
		String pin = rand.nextInt(10000) + "";  // 0000 - 9999
		while(!validPin(pin)){
			pin = rand.nextInt(10000) + "";  // 0000 - 9999
		}
		return pin;
	}

    @Override
    public List<Employee> getEmployeesPunchStatus(List<Employee> empList) {
        empList.forEach(emp -> {
            EmployeeClocking employeeClocking = employeeClockingRepository.getLastClockingByEmployee(emp.getId());
            if (employeeClocking != null) {
                emp.setPunchStatus(VisitStatusEnum.CHECK_IN.toString());
                if (employeeClocking.getCheckOutTime() != null) {
                    emp.setPunchStatus(VisitStatusEnum.CHECK_OUT.toString());
                }
            }
        });
        return empList;
    }*/

	@Override
	public Page<Driver> findByCompanyIdAndAgency(Long companyId, Long agencyId, Pageable pageable) {
		return driverRepository.findByCompanyIdAndAgency(companyId, agencyId, pageable);
	}

	@Override
	public Page<Driver> findByCompanyId(Long companyId, Pageable pageable) {
		return driverRepository.findByCompanyId(companyId, pageable);
	}

	@Override
	public List<Driver> findByCompanyId(Long companyId) {
		return driverRepository.findByCompanyId(companyId);
	}

	@Override
	public List<Driver> findByCompanyIdAndAgency(Long companyId, Long agencyId) {
		return driverRepository.findByCompanyIdAndAgency(companyId, agencyId);
	}

	@Override
	public List<Driver> searchByFirstName(String search) {
		return driverRepository.searchByFirstName(search);
	}

	@Override
	public List<Driver> searchByFirstNameByCompany(Long companyId, String search) {
		return driverRepository.searchByFirstNameByCompany(companyId, search);
	}

	@Override
	public List<Driver> searchByFirstNameByCompanyAgency(Long companyId, Long agencyId, String search) {
		return driverRepository.searchByFirstNameByCompanyAgency(companyId, agencyId, search);
	}

	@Override
	public Page<Driver> getDriverLicenseEndNextDays(int days, Admin permissionAdmin, Pageable pageable, Long companyId, Long agencyId) {
		if (days < 0)
			return new PageImpl<Driver>(new ArrayList<>());
		DateTime currentDate = new DateTime();
		DateTime today = currentDate.minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
		DateTime endDay = currentDate.plusDays(days).plusDays(days).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);

		Page<Driver> returnedPages = null;

		if (companyId != null) {
			if (agencyId != null) {
				returnedPages = driverRepository.getDriverLicenseEndFromDayToDayByCompanyAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
						companyId, agencyId, pageable);
			} else {
				returnedPages = driverRepository.getDriverLicenseEndFromDayToDayByCompany(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
						companyId, pageable);
			}

		} else {
			if(isSuperAdmin(permissionAdmin)){
				returnedPages = driverRepository.getDriverLicenseEndFromDayToDay(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()), pageable);
			}else if (isCompanyAdmin(permissionAdmin)){
				AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

				if (adminAgencyCompanyOrganization != null) {
					returnedPages = driverRepository.getDriverLicenseEndFromDayToDayByCompany(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
							adminAgencyCompanyOrganization.getCompany().getId(), pageable);
				}
			}else if (isAgencyAdmin(permissionAdmin)) {
				AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

				if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
					returnedPages = driverRepository.getDriverLicenseEndFromDayToDayByCompanyAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
							adminAgencyCompanyOrganization.getCompany().getId(), adminAgencyCompanyOrganization.getAgency().getId(), pageable);
				}
			}
		}

		return returnedPages;
	}

	@Override
	public Page<Driver> getDriverTlcFhvLicenseEndNextDays(int days, Admin permissionAdmin, Pageable pageable, Long companyId, Long agencyId) {
		if (days < 0)
			return new PageImpl<Driver>(new ArrayList<>());
		DateTime currentDate = new DateTime();
		DateTime today = currentDate.minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
		DateTime endDay = currentDate.plusDays(days).plusDays(days).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);

		Page<Driver> returnedPages = null;

		if (companyId != null) {
			if (agencyId != null) {
				returnedPages = driverRepository.getDriverTLCFhvLicenseEndFromDayToDayByCompanyAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
						companyId, agencyId, pageable);
			} else {
				returnedPages = driverRepository.getDriverTLCFhvLicenseEndFromDayToDayByCompany(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
						companyId, pageable);
			}

		} else {
			if(isSuperAdmin(permissionAdmin)){
				returnedPages = driverRepository.getDriverTLCFhvLicenseEndFromDayToDay(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()), pageable);
			}else if (isCompanyAdmin(permissionAdmin)){
				AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

				if (adminAgencyCompanyOrganization != null) {
					returnedPages = driverRepository.getDriverTLCFhvLicenseEndFromDayToDayByCompany(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
							adminAgencyCompanyOrganization.getCompany().getId(), pageable);
				}
			}else if (isAgencyAdmin(permissionAdmin)) {
				AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

				if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
					returnedPages = driverRepository.getDriverTLCFhvLicenseEndFromDayToDayByCompanyAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
							adminAgencyCompanyOrganization.getCompany().getId(), adminAgencyCompanyOrganization.getAgency().getId(), pageable);
				}
			}
		}


		return returnedPages;
	}

	@Override
	public Page<Driver> getDriverBackgroundCheckEndNextDays(int days, Admin permissionAdmin, Pageable pageable, Long companyId, Long agencyId) {
		if (days < 0)
			return new PageImpl<Driver>(new ArrayList<>());
		DateTime currentDate = new DateTime();
		DateTime today = currentDate.minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
		DateTime endDay = currentDate.plusDays(days).plusDays(days).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);

		Page<Driver> returnedPages = null;

		if (companyId != null) {
			if (agencyId != null) {
				returnedPages = driverRepository.getDriverBackgroundCheckEndFromDayToDayByCompanyAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
						companyId, agencyId, pageable);
			} else {
				returnedPages = driverRepository.getDriverBackgroundCheckEndFromDayToDayByCompany(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
						companyId, pageable);
			}

		} else {
			if(isSuperAdmin(permissionAdmin)){
				returnedPages = driverRepository.getDriverBackgroundCheckEndFromDayToDay(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()), pageable);
			}else if (isCompanyAdmin(permissionAdmin)){
				AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

				if (adminAgencyCompanyOrganization != null) {
					returnedPages = driverRepository.getDriverBackgroundCheckEndFromDayToDayByCompany(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
							adminAgencyCompanyOrganization.getCompany().getId(), pageable);
				}
			}else if (isAgencyAdmin(permissionAdmin)) {
				AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

				if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
					returnedPages = driverRepository.getDriverBackgroundCheckEndFromDayToDayByCompanyAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
							adminAgencyCompanyOrganization.getCompany().getId(), adminAgencyCompanyOrganization.getAgency().getId(), pageable);
				}
			}
		}


		return returnedPages;
	}

	@Override
	public Page<Driver> getDriverDrivingRecordEndNextDays(int days, Admin permissionAdmin, Pageable pageable, Long companyId, Long agencyId) {
		if (days < 0)
			return new PageImpl<Driver>(new ArrayList<>());
		DateTime currentDate = new DateTime();
		DateTime today = currentDate.minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
		DateTime endDay = currentDate.plusDays(days).plusDays(days).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);

		Page<Driver> returnedPages = null;

		if (companyId != null) {
			if (agencyId != null) {
				returnedPages = driverRepository.getDriverDrivingRecordEndFromDayToDayByCompanyAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
						companyId, agencyId, pageable);
			} else {
				returnedPages = driverRepository.getDriverDrivingRecordEndFromDayToDayByCompany(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
						companyId, pageable);
			}

		} else {
			if(isSuperAdmin(permissionAdmin)){
				returnedPages = driverRepository.getDriverDrivingRecordEndFromDayToDay(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()), pageable);
			}else if (isCompanyAdmin(permissionAdmin)){
				AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

				if (adminAgencyCompanyOrganization != null) {
					returnedPages = driverRepository.getDriverDrivingRecordEndFromDayToDayByCompany(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
							adminAgencyCompanyOrganization.getCompany().getId(), pageable);
				}
			}else if (isAgencyAdmin(permissionAdmin)) {
				AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

				if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
					returnedPages = driverRepository.getDriverDrivingRecordEndFromDayToDayByCompanyAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
							adminAgencyCompanyOrganization.getCompany().getId(), adminAgencyCompanyOrganization.getAgency().getId(), pageable);
				}
			}
		}

		return returnedPages;
	}

	@Override
	public Page<Driver> getDriverDrugScreenEndNextDays(int days, Admin permissionAdmin, Pageable pageable, Long companyId, Long agencyId) {
		if (days < 0)
			return new PageImpl<Driver>(new ArrayList<>());
		DateTime currentDate = new DateTime();
		DateTime today = currentDate.minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
		DateTime endDay = currentDate.plusDays(days).plusDays(days).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);

		Page<Driver> returnedPages = null;

		if (companyId != null) {
			if (agencyId != null) {
				returnedPages = driverRepository.getDriverDrugScreenEndFromDayToDayByCompanyAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
						companyId, agencyId, pageable);
			} else {
				returnedPages = driverRepository.getDriverDrugScreenEndFromDayToDayByCompany(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
						companyId, pageable);
			}

		} else {
			if(isSuperAdmin(permissionAdmin)){
				returnedPages = driverRepository.getDriverDrugScreenEndFromDayToDay(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()), pageable);
			}else if (isCompanyAdmin(permissionAdmin)){
				AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

				if (adminAgencyCompanyOrganization != null) {
					returnedPages = driverRepository.getDriverDrugScreenEndFromDayToDayByCompany(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
							adminAgencyCompanyOrganization.getCompany().getId(), pageable);
				}
			}else if (isAgencyAdmin(permissionAdmin)) {
				AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

				if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
					returnedPages = driverRepository.getDriverDrugScreenEndFromDayToDayByCompanyAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
							adminAgencyCompanyOrganization.getCompany().getId(), adminAgencyCompanyOrganization.getAgency().getId(), pageable);
				}
			}
		}

		return returnedPages;
	}

}
