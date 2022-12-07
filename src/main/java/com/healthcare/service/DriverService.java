package com.healthcare.service;

import java.util.List;

import com.healthcare.model.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.healthcare.model.entity.Driver;
import com.healthcare.model.entity.Employee;

/**
 * Created by Jean Antunes on 11/05/17.
 */
public interface DriverService  extends IService<Driver>, IFinder<Driver>{

	Driver save(Driver driver);

	List<Driver> findAll();
	
	/*Employee savePunch(Employee employee);

	List<Employee> findAll();
	
	List<Employee> findByCampanyIdAndAgencyId(Long companyId, Long agencyId);

	List<Employee> findEmployeesCrossingWorkLimit(int hoursDifference);
	
	List<Employee> getEmployeesPunchStatus(List<Employee> empList);

	public List<EmployeeClocking> getTotalWorkingTimeThisWeek(Long id);
	
	boolean validPin(String pin);
	
	String generatePin();*/

	Page<Driver> findByCompanyId(Long companyId, Pageable pageable);

	Page<Driver> findByCompanyIdAndAgency(Long companyId, Long agencyId, Pageable pageable);

	List<Driver> findByCompanyId(Long companyId);

	List<Driver> findByCompanyIdAndAgency(Long companyId, Long agencyId);

	List<Driver> searchByFirstName(String search);

	List<Driver> searchByFirstNameByCompany(Long companyId, String search);

	List<Driver> searchByFirstNameByCompanyAgency(Long companyId, Long agencyId, String search);

	public Page<Driver> getDriverLicenseEndNextDays(int days, Admin permissionAdmin, Pageable pageable, Long companyId, Long agencyId);

	public Page<Driver> getDriverTlcFhvLicenseEndNextDays(int days, Admin permissionAdmin, Pageable pageable, Long companyId, Long agencyId);

	public Page<Driver> getDriverBackgroundCheckEndNextDays(int days, Admin permissionAdmin, Pageable pageable, Long companyId, Long agencyId);

	public Page<Driver> getDriverDrivingRecordEndNextDays(int days, Admin permissionAdmin, Pageable pageable, Long companyId, Long agencyId);

	public Page<Driver> getDriverDrugScreenEndNextDays(int days, Admin permissionAdmin, Pageable pageable, Long companyId, Long agencyId);
}
