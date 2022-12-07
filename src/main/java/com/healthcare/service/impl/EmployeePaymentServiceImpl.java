package com.healthcare.service.impl;

import com.healthcare.model.entity.Employee;
import com.healthcare.model.entity.EmployeePayment;
import com.healthcare.repository.EmployeePaymentRepository;
import com.healthcare.repository.EmployeeRepository;
import com.healthcare.service.EmployeePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class EmployeePaymentServiceImpl extends BasicService<EmployeePayment, EmployeePaymentRepository> implements EmployeePaymentService {

    private static final String KEY = EmployeePayment.class.getSimpleName();

    @Autowired
    private EmployeePaymentRepository employeePaymentRepository;

    @Autowired
    private RedisTemplate<String, EmployeePayment> employeeEmployeeRedisTemplate;

    @Override
    public EmployeePayment save(EmployeePayment employeePayment) {
        employeePayment = employeePaymentRepository.save(employeePayment);
        employeeEmployeeRedisTemplate.opsForHash().put(KEY, employeePayment.getId(), employeePayment);

        return employeePayment;
    }

    @Override
    @Transactional
    public Long deleteById(Long id) {
        employeePaymentRepository.delete(id);
        return employeeEmployeeRedisTemplate.opsForHash().delete(KEY, id);
    }

    @Override
    @Transactional
    public Long disableById(Long id) {
        return null;
    }

    @Override
    @Transactional
    public EmployeePayment findById(Long id) {
        EmployeePayment employeePayment = employeePaymentRepository.findOne(id);
        return employeePayment;
    }

    @Override
    @Transactional
    public Page<EmployeePayment> findPaymentByEmployee(Long employeeId, Date startDate, Date endDate, Pageable pageable) {
        return employeePaymentRepository.findPaymentByEmployee(employeeId, startDate, endDate, pageable);
    }

    @Override
    @Transactional
    public Page<EmployeePayment> findPaymentByAgency(Long agencyId, Date startDate, Date endDate, Pageable pageable) {
        return employeePaymentRepository.findPaymentByAgency(agencyId, startDate, endDate, pageable);
    }

    @Override
    @Transactional
    public Page<EmployeePayment> findPaymentByCompany(Long companyId, Date startDate, Date endDate, Pageable pageable) {
        return employeePaymentRepository.findPaymentByCompany(companyId, startDate, endDate, pageable);
    }

    @Override
    @Transactional
    public Page<EmployeePayment> findAllPayments( Date startDate, Date endDate, Pageable pageable) {
        return employeePaymentRepository.findAllPayments(startDate, endDate, pageable);
    }

    @Override
    @Transactional
    public Page<EmployeePayment> calculatePaymentByTimeRange(Long companyId, Long agencyId, Long employeeId,
                                                             Date startDate, Date endDate, Pageable pageable) {
    	startDate.setHours(0);
    	startDate.setMinutes(0);
    	startDate.setSeconds(0);
    	endDate.setHours(23);
    	endDate.setMinutes(59);
    	endDate.setSeconds(59);
        List<EmployeePayment> employeePayments = employeePaymentRepository.findAllPaymentsList(startDate, endDate);
        if (companyId != null && companyId > 0) {
            employeePayments = employeePaymentRepository.findPaymentByCompanyList(companyId, startDate, endDate);
        } else if (agencyId != null && agencyId > 0) {
            employeePayments = employeePaymentRepository.findPaymentByAgencyList(agencyId, startDate, endDate);
        } else if (employeeId != null && employeeId > 0) {
            employeePayments = employeePaymentRepository.findPaymentByEmployeeList(employeeId, startDate, endDate);
        }




        Map<Long, ArrayList<EmployeePayment>> empPaymentMap = new HashMap<Long, ArrayList<EmployeePayment>>();

        if (employeePayments != null && employeePayments.size() > 0) {
            for (EmployeePayment empPayment: employeePayments) {
                if (empPayment.getEmployee() != null) {
                    if (empPaymentMap.containsKey(empPayment.getEmployee().getId())) {
                        ArrayList<EmployeePayment> emps = empPaymentMap.get(empPayment.getEmployee().getId());
                        emps.add(empPayment);
                        empPaymentMap.put(empPayment.getEmployee().getId(), emps);
                    } else {
                        ArrayList<EmployeePayment> emps = new ArrayList<EmployeePayment>();
                        emps.add(empPayment);
                        empPaymentMap.put(empPayment.getEmployee().getId(), emps);
                    }
                }
            }
        }

        /*Double employeeRate = null;
		if("HOURLY".equalsIgnoreCase(employee.getEmployeeType().getValue())) {
            employeeRate = employee.getRate();
        }else if("SALARY".equalsIgnoreCase(employee.getEmployeeType().getValue())){
            employeeRate = employee.getRate()/52/40;
        }*/

        List<EmployeePayment> output = new ArrayList<EmployeePayment>();
        Iterator<Long> keys = empPaymentMap.keySet().iterator();
        while (keys.hasNext()) {
            Long key = keys.next();
            ArrayList<EmployeePayment> empPayments = empPaymentMap.get(key);
            // total hours
            // total payment
            Double totalPayment = 0.0;
            Double totalHours   = 0.0;
            Double totalAdjustment = 0.0;
            Double totalWorkHours = 0.0;
            Double totalSpreadOfHours = 0.0;

            EmployeePayment processed = null;
            if (empPayments != null && empPayments.size() > 0 ) {
                for (EmployeePayment employeePayment: empPayments) {
                    Double employeeRate = null;
                    if("HOURLY".equalsIgnoreCase(employeePayment.getEmployee().getEmployeeType().getValue())) {
                        employeeRate = employeePayment.getEmployee().getRate();
                    }else if("SALARY".equalsIgnoreCase(employeePayment.getEmployee().getEmployeeType().getValue())){
                        employeeRate = employeePayment.getEmployee().getRate()/52/40;
                    }
                    totalWorkHours += employeePayment.getWorkHours();
                    totalSpreadOfHours += employeePayment.getSpreadOfHour();
                    totalHours = totalWorkHours + totalSpreadOfHours;
                    totalPayment += employeeRate * totalHours + employeePayment.getAdjustment();
                    processed = employeePayment;
                }
            }

            if (processed != null) {
                // getting new object
                EmployeePayment employeePayment = new EmployeePayment();
                employeePayment.setId(processed.getId());
                employeePayment.setSpreadOfHour(totalSpreadOfHours);
                employeePayment.setWorkHours(totalWorkHours);
                employeePayment.setTotalHours(totalHours);
                employeePayment.setTotalPayment(totalPayment);
                employeePayment.setAdjustment(processed.getAdjustment());
                employeePayment.setEmployee(processed.getEmployee());
                output.add(employeePayment);
            }

        }

        PageImpl<EmployeePayment> page = new PageImpl<EmployeePayment>(output, pageable, output.size());

        return page;
    }

    @Override
    @Transactional
    public Page<EmployeePayment> calculatePaymentByTimeRangeEmployee(Long employeeId, Date startDate, Date endDate, Pageable pageable) {
        return null;
    }

    @Override
    @Transactional
    public Page<EmployeePayment> calculatePaymentByTimeRangeAgency(Long agencyId, Date startDate, Date endDate, Pageable pageable) {
        return null;
    }

    @Override
    @Transactional
    public Page<EmployeePayment> calculatePaymentByTimeRangeCompany(Long companyId, Date startDate, Date endDate, Pageable pageable) {
        return null;
    }
}
