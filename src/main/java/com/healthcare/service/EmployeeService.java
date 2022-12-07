package com.healthcare.service;

import java.util.List;

import com.healthcare.api.model.ExpressEmployeeRequestDTO;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Employee;
import com.healthcare.model.entity.EmployeeClocking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Jean Antunes on 11/05/17.
 */
public interface EmployeeService extends IService<Employee>, IFinder<Employee> {

	Employee save(Employee employee);
	
	Employee savePunch(Employee employee);

	List<Employee> findAll();
	
	List<Employee> findByCampanyIdAndAgencyId(Long companyId, Long agencyId);

	List<Employee> findEmployeesCrossingWorkLimit(int hoursDifference);

	Integer resetEmployeesWeeklyHours();
	
	List<Employee> getEmployeesPunchStatus(List<Employee> empList);
	
	Employee getEmployeesPunchStatus(Employee employee);

	public List<EmployeeClocking> getTotalWorkingTimeThisWeek(Long id);
	
	boolean validPin(String pin);
	
	String generatePin();

	List<Employee> findEmployeesOvertime(Admin permissionAdmin);

	void calculateSickDay();
	
	Employee getEmployeeByPin(String pin);

	public Page<Employee> getAuthorizationEmployeeBornFromDateToDate(int days, Admin permissionAdmin, Pageable pageable);
	Page<Employee> findByCompany(Long companyId, Pageable pageable);

	Page<Employee> findByCpmAndAgency(Long companyId, Long agencyId, Pageable pageable);

	List<Employee> findByCpmAndAgencyList(Long companyId, Long agencyId);

	public Page<Employee> getVacationEmployeeReminder(int days, Admin permissionAdmin, Pageable pageable);

	List<Employee> findByCompany(Long companyId);

	ExpressEmployeeRequestDTO expressCheckin(ExpressEmployeeRequestDTO expressEmployeeRequestDTO, Employee employee);
}
