package com.healthcare.service.impl;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.*;

import javax.transaction.Transactional;

import com.healthcare.api.model.ExpressEmployeeRequestDTO;
import com.healthcare.model.entity.*;
import com.healthcare.model.enums.EmployeeStatusEnum;
import com.healthcare.repository.*;
import com.healthcare.service.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.healthcare.model.enums.EntityStatusEnum;
import com.healthcare.model.enums.VisitStatusEnum;
import com.healthcare.util.DateUtils;

import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

@Service
@Transactional
public class EmployeeServiceImpl extends BasicService<Employee, EmployeeRepository> implements EmployeeService {
	private static final String KEY = Employee.class.getSimpleName();

	@Autowired
	EmployeeRepository employeeRepository;
	@Autowired
    private UserRepository userRepository;
	@Autowired
	private LeaveService employeeLeaveService;

	@Autowired
	private EmployeeClockingRepository employeeClockingRepository;

	@Autowired
	private LeaveRepository leaveRepository;

	@Autowired
	private AdminAgencyCompanyOrganizationRepository adminAgencyCompanyOrganizationRepository;

	@Autowired
	private RedisTemplate<String, Employee> employeeRedisTemplate;

	@Autowired
	private EmployeePaymentRepository employeePaymentRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private AgencyService agencyService;

	@Autowired
	private DocumentService documentService;

	@Override
	@Transactional
	public Employee save(Employee employee) {
		if(employee.getId() != null){
			employee.setUpdatedAt(new Timestamp(new Date().getTime()));
	  	} else {
	  		employee.setCreatedAt(new Timestamp(new Date().getTime()));
	  		employee.setUpdatedAt(new Timestamp(new Date().getTime()));
	  	}
		if(employee.getDateOfBirth() != null){
			employee.getDateOfBirth().setHours(12);
			employee.getDateOfBirth().setMinutes(0);
			employee.getDateOfBirth().setSeconds(0);
    	}
		if(employee.getVacationStart() != null){
			employee.getVacationStart().setHours(12);
			employee.getVacationStart().setMinutes(0);
			employee.getVacationStart().setSeconds(0);
    	}
		if(employee.getVacationEnd() != null){
			employee.getVacationEnd().setHours(12);
			employee.getVacationEnd().setMinutes(0);
			employee.getVacationEnd().setSeconds(0);
    	}
		if(employee.getWorkStart() != null){
			employee.getWorkStart().setHours(12);
			employee.getWorkStart().setMinutes(0);
			employee.getWorkStart().setSeconds(0);
    	}
		if(employee.getWorkEnd() != null){
			employee.getWorkEnd().setHours(12);
			employee.getWorkEnd().setMinutes(0);
			employee.getWorkEnd().setSeconds(0);
    	}
		if(employee.getCertificateStart() != null){
			employee.getCertificateStart().setHours(12);
			employee.getCertificateStart().setMinutes(0);
			employee.getCertificateStart().setSeconds(0);
    	}
		if(employee.getCertificateEnd() != null){
			employee.getCertificateEnd().setHours(12);
			employee.getCertificateEnd().setMinutes(0);
			employee.getCertificateEnd().setSeconds(0);
    	}
		employee = employeeRepository.save(employee);
		employeeRedisTemplate.opsForHash().put(KEY, employee.getId(), employee);
		return employee;
	}

	@Override
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
	public Integer resetEmployeesWeeklyHours() {
		return employeeRepository.resetEmployeesWeeklyHours("0h0m");
	}

	@Override
    @Transactional
    public Employee savePunch(Employee dbEmp) {
        EmployeeClocking employeeClocking = employeeClockingRepository.getLastClockingByEmployee(dbEmp.getId());
        if(employeeClocking==null || employeeClocking.getCheckOutTime()!=null) {
			insertPayment(dbEmp, employeeClocking);
        employeeClocking = new EmployeeClocking();
        employeeClocking.setCheckInSignatureId(dbEmp.getRulesAndRegusDocId());
        employeeClocking.setCheckInTime(new Timestamp(new Date().getTime()));
        employeeClocking.setEmployee(dbEmp);
        }else{
        	employeeClocking.setCheckOutTime(new Timestamp(new Date().getTime()));

			updatePayment(dbEmp, employeeClocking);
        }
        employeeClockingRepository.save(employeeClocking);
        //dbEmp.setWeeklyHoursWorked("0h0m");
        return save(dbEmp);
    }

	private void updatePayment(Employee dbEmp, EmployeeClocking employeeClocking) {
		EmployeePayment employeePayment = employeePaymentRepository.getLastPaymentByEmployee(dbEmp.getId());
		if(employeePayment != null){
            Long timeDiff = (employeeClocking.getCheckOutTime().getTime() - employeeClocking.getCheckInTime().getTime());
            Double diff = timeDiff.doubleValue()/(60 * 60 * 1000);
            employeePayment.setWorkHours(diff + employeePayment.getWorkHours());
			calculateSpreadHours(dbEmp, employeePayment);
			employeePaymentRepository.save(employeePayment);

		}
	}

	private void calculateSpreadHours(Employee employee, EmployeePayment employeePayment) {
		Double employeeRate = null;
		if("HOURLY".equalsIgnoreCase(employee.getEmployeeType().getValue())) {
            employeeRate = employee.getRate();
        }else if("SALARY".equalsIgnoreCase(employee.getEmployeeType().getValue())){
            employeeRate = employee.getRate()/52/40;
        }

		Double minWorkShift = 10.0;
		Double minimumWage = 0.0;
		if(employee.getAgency() != null){
            minimumWage = employee.getAgency().getMinimumWage();
        }
		Double minimumWageForShift = minimumWage * minWorkShift;
		Double actualPayForShift = employeeRate * employeePayment.getWorkHours();
		Double zero = new Double(0);
		if(employeePayment.getWorkHours()>=minWorkShift){
            Double difference = actualPayForShift - minimumWageForShift;
            if(difference < employeeRate) {
                Double adjustment = employeeRate - difference;
                Double spreadHours = adjustment/employeeRate;
                employeePayment.setAdjustment(adjustment);
                employeePayment.setSpreadOfHour(spreadHours);
				employeePayment.setPaymentForThatDay(minimumWageForShift + minimumWage);
            } else {
				employeePayment.setAdjustment(zero);
				employeePayment.setSpreadOfHour(zero);
				employeePayment.setPaymentForThatDay(actualPayForShift);
			}
        } else {
			employeePayment.setAdjustment(zero);
			employeePayment.setSpreadOfHour(zero);
			employeePayment.setPaymentForThatDay(actualPayForShift);
		}
	}

	private void insertPayment(Employee dbEmp, EmployeeClocking employeeClocking) {
		EmployeePayment employeePayment = new EmployeePayment();
		Date checkInTime = new Timestamp(new Date().getTime());
		Date prevCheckoutTime = checkInTime;
		if(employeeClocking != null){
            prevCheckoutTime = employeeClocking.getCheckOutTime();
        }

		Long time = checkInTime.getTime() - prevCheckoutTime.getTime();
		Double difference = time.doubleValue()/(60 * 60 * 1000);
		employeePayment = employeePaymentRepository.getLastPaymentByEmployee(dbEmp.getId());
		if(employeePayment != null && difference<5){
            employeePayment.setWorkHours(difference + employeePayment.getWorkHours());
            employeePaymentRepository.save(employeePayment);
        }else{
			employeePayment = new EmployeePayment();
			employeePayment.setEmployee(dbEmp);
			employeePayment.setWorkHours(0.0);
			employeePayment.setDate(new Date());
            employeePaymentRepository.save(employeePayment);
        }
		calculateSpreadHours(dbEmp, employeePayment);
	}

	@Override
	public boolean validPin(String pin) {
		Employee emp = employeeRepository.findByPin(pin);
		if(emp == null){
			User user = userRepository.findUserByPIN(pin);
			if(user == null)
				return true;
			else 
				return false;// invalid pin - exists in db
		}else{
			return false; // invalid pin - exists in db
		}
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
    }
    
    @Override
    public Employee getEmployeesPunchStatus(Employee employee) {
        
            EmployeeClocking employeeClocking = employeeClockingRepository.getLastClockingByEmployee(employee.getId());
            if (employeeClocking != null) {
            	employee.setPunchStatus(VisitStatusEnum.CHECK_IN.toString());
                if (employeeClocking.getCheckOutTime() != null) {
                	employee.setPunchStatus(VisitStatusEnum.CHECK_OUT.toString());
                }
            }
        
        return employee;
    }

    @Override
	public List<Employee> findEmployeesOvertime(Admin permissionAdmin) {
		DateTime startDate = new DateTime(DateUtils.getFirstDayOfCurrentWeek());
		DateTime endDate = new DateTime(DateUtils.getLastDayOfCurrentWeek());

		List<EmployeeClocking> clockings = null;
		if(isSuperAdmin(permissionAdmin)){
			clockings = employeeClockingRepository.findAllByStartDateEndDate(startDate.toDate(), endDate.toDate());
		} else if (isCompanyAdmin(permissionAdmin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

			if (adminAgencyCompanyOrganization != null) {
				clockings = employeeClockingRepository.findAllByStartDateEndDateByCompany(
						startDate.toDate(), endDate.toDate(), adminAgencyCompanyOrganization.getCompany().getId());
			}

		} else if (isAgencyAdmin(permissionAdmin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				clockings = employeeClockingRepository.findAllByStartDateEndDateByCompanyAgency(startDate.toDate(),
						endDate.toDate(), adminAgencyCompanyOrganization.getCompany().getId(), adminAgencyCompanyOrganization.getAgency().getId());
			}
		}

		List<Employee> employeesOverTime = new ArrayList<Employee>();
		if (clockings != null) {
			// convert to mapping
			LinkedHashMap<Long, ArrayList<EmployeeClocking>> convertedMap = new LinkedHashMap<Long, ArrayList<EmployeeClocking>>();

			for (EmployeeClocking clocking: clockings) {
				Long employeeKey = clocking.getEmployee().getId();
				if (convertedMap.containsKey(clocking.getEmployee().getId())) {
					convertedMap.get(employeeKey).add(clocking);
				} else {
					ArrayList<EmployeeClocking> convertedVisit = new ArrayList<EmployeeClocking>();
					convertedVisit.add(clocking);
					convertedMap.put(employeeKey, convertedVisit);
				}
			}

			Iterator<Long> keys = convertedMap.keySet().iterator();
			while (keys.hasNext()) {
				Long key = keys.next();
				ArrayList<EmployeeClocking> followedEmployees = convertedMap.get(key);
				long diff = 0;
				for (EmployeeClocking employeeClocking: followedEmployees) {
					diff += employeeClocking.getCheckOutTime().getTime() - employeeClocking.getCheckInTime().getTime();
				}
				long sumWorkedMintues = diff / (60 * 1000) % 60;
				long sumWorkedHours = diff / (60 * 60 * 1000);

				Employee employeeWO = followedEmployees.get(0).getEmployee();
				if (employeeWO.getWeeklyWorkingTimeLimitation() != null && sumWorkedHours > employeeWO.getWeeklyWorkingTimeLimitation()) {
					employeeWO.setWeeklyHoursWorked(sumWorkedHours + "h" + sumWorkedMintues + "m");
					employeesOverTime.add(employeeWO);
					// update weekly worked hour
					employeeRepository.save(employeeWO);
				}
			}
		}

		return employeesOverTime;
	}

	@Override
	public void calculateSickDay() {
		List<EmployeeClocking> employeeClockings = employeeClockingRepository.findAllByAccrualTime();
		List<Employee> employeeSickdays = new ArrayList<Employee>();
		if (employeeClockings != null) {
			// convert to mapping
			LinkedHashMap<Long, ArrayList<EmployeeClocking>> convertedMap = new LinkedHashMap<Long, ArrayList<EmployeeClocking>>();

			for (EmployeeClocking clocking : employeeClockings) {
				Long employeeKey = clocking.getEmployee().getId();
				if (convertedMap.containsKey(clocking.getEmployee().getId())) {
					convertedMap.get(employeeKey).add(clocking);
				} else {
					ArrayList<EmployeeClocking> convertedVisit = new ArrayList<EmployeeClocking>();
					convertedVisit.add(clocking);
					convertedMap.put(employeeKey, convertedVisit);
				}
			}

			// process sickday here

			Iterator<Long> keys = convertedMap.keySet().iterator();
			while (keys.hasNext()) {
				Long key = keys.next();
				ArrayList<EmployeeClocking> followedEmployees = convertedMap.get(key);
				long sumWorkedHours = 0;
				for (EmployeeClocking employeeClocking: followedEmployees) {
					long diff = employeeClocking.getCheckOutTime().getTime() - employeeClocking.getCheckInTime().getTime();
					long diffHours = diff / (60 * 60 * 1000);
					sumWorkedHours += diffHours;
				}

				Employee employeeSickDay = followedEmployees.get(0).getEmployee();
				// calculate sick day here
				Integer sickDays = (int) sumWorkedHours/30;
				// get leave information here
				List<Leave> leaves = leaveRepository.findByEmployee(employeeSickDay.getId());
				if (leaves != null && leaves.size() > 0) {
					Leave leave = leaves.get(0);
					if (sickDays > leave.getSickDaysLimitation()) {
						sickDays = leave.getSickDaysLimitation();
					}

					leave.setSickDays(sickDays);
					// update sick day for leave with employee
					leaveRepository.save(leave);
				}
				//employeeRepository.save(employeeSickDay);
			}
		}
	}

	@Override
	public Employee getEmployeeByPin(String pin) {
		Employee emp = employeeRepository.findByPin(pin);
		return emp;
	}

	public Page<Employee> getAuthorizationEmployeeBornFromDateToDate(int days, Admin permissionAdmin, Pageable pageable) {
		if (days < 0)
			return new PageImpl<Employee>(new ArrayList<>());

		List<Employee> e = new ArrayList<Employee>();

		if (isSuperAdmin(permissionAdmin)) {
			e = employeeRepository.getAuthorizationEmployeeBornFromDateToDate(new Integer(days));
		} else if (isCompanyAdmin(permissionAdmin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

			if (adminAgencyCompanyOrganization != null) {
				e = employeeRepository.getAuthorizationEmployeeBornFromDateToDateByCompany(days, adminAgencyCompanyOrganization.getCompany().getId());
			}
		} else if (isAgencyAdmin(permissionAdmin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				e = employeeRepository.getAuthorizationEmployeeBornFromDateToDateByCompanyAgency(days,
						adminAgencyCompanyOrganization.getCompany().getId(), adminAgencyCompanyOrganization.getAgency().getId());
			}
		}

		Collections.sort(e, new Comparator<Employee>() {
			public int compare(Employee e1, Employee e2) {
				Date date= new Date();
				Calendar currentDate = Calendar.getInstance();
				currentDate.setTime(date);

				Calendar e1DOB = Calendar.getInstance();
				e1DOB.setTime(e1.getDateOfBirth());
				e1DOB.add(Calendar.YEAR, - e1DOB.get(Calendar.YEAR));
				e1DOB.add(Calendar.YEAR, + currentDate.get(Calendar.YEAR));

				Calendar e2DOB = Calendar.getInstance();
				e2DOB.setTime(e2.getDateOfBirth());
				e2DOB.add(Calendar.YEAR, - e2DOB.get(Calendar.YEAR));
				e2DOB.add(Calendar.YEAR, + currentDate.get(Calendar.YEAR));


				currentDate.add(Calendar.MONTH, +1);

				return e1DOB.compareTo(e2DOB);
			}
		});

		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > e.size() ? e.size() : (start + pageable.getPageSize());
		Page<Employee> pages = new PageImpl<Employee>(e.subList(start, end), pageable, e.size());
		return pages;
	}

	@Override
	public Page<Employee> findByCompany(Long companyId, Pageable pageable) {

		return employeeRepository.findByCompanyId(companyId, pageable);
	}

	@Override
	public Page<Employee> findByCpmAndAgency(Long companyId, Long agencyId, Pageable pageable)  {
		return employeeRepository.findByCompanyIdANDAgencyId(companyId, agencyId, pageable);
	}

	@Override
	public List<Employee> findByCpmAndAgencyList(Long companyId, Long agencyId)  {
		return employeeRepository.findByCpmAndAgencyList(companyId, agencyId);
	}

	@Override
	public Page<Employee> getVacationEmployeeReminder(int days, Admin permissionAdmin, Pageable pageable) {
		if (days < 0)
			return new PageImpl<Employee>(new ArrayList<>());
		DateTime today = new DateTime();
		DateTime endDay = today.plusDays(days);

		Page<Employee> returnedPages = null;

		if(isSuperAdmin(permissionAdmin)){
			returnedPages = employeeRepository.getVacationEmployeeFromDayToDay(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()), pageable);
		} else if (isCompanyAdmin(permissionAdmin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

			if (adminAgencyCompanyOrganization != null) {
				returnedPages = employeeRepository.getVacationEmployeeFromDayToDayByCompany(
						new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
						adminAgencyCompanyOrganization.getCompany().getId(), pageable);
			}

		} else if (isAgencyAdmin(permissionAdmin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				returnedPages = employeeRepository.getVacationEmployeeFromDayToDayByCompanyAgency(
						new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
						adminAgencyCompanyOrganization.getCompany().getId(), adminAgencyCompanyOrganization.getAgency().getId(), pageable);
			}
		}

		return returnedPages;
	}

	@Override
	@Transactional
	public List<Employee> findByCompany(Long companyId) {
		return employeeRepository.findByCompanyId(companyId);
	}

	@Override
	public ExpressEmployeeRequestDTO expressCheckin(ExpressEmployeeRequestDTO expressEmployeeRequestDTO, Employee employee) {

		Document document = new Document();
		if(expressEmployeeRequestDTO.getSignature() != null){
			document = documentService.save(expressEmployeeRequestDTO.getSignature());
		}


		EmployeeClocking employeeClocking = employeeClockingRepository.getLastClockingByEmployee(employee.getId());
		if(employeeClocking==null || employeeClocking.getCheckOutTime()!=null) {
			insertPayment(employee, employeeClocking);
			employeeClocking = new EmployeeClocking();
			if(document.getId()!=null){
				employeeClocking.setCheckInSignatureId(document);
			}
			employeeClocking.setCheckInTime(new Timestamp(new Date().getTime()));
			employeeClocking.setEmployee(employee);
		}else{
			employeeClocking.setCheckOutTime(new Timestamp(new Date().getTime()));
			if(document.getId()!=null){
				employeeClocking.setCheckOutSignatureId(document);
			}
			updatePayment(employee, employeeClocking);
		}
		employeeClockingRepository.save(employeeClocking);
		save(employee);
		expressEmployeeRequestDTO.setId(employee.getId().intValue());

		return expressEmployeeRequestDTO;
	}

}
