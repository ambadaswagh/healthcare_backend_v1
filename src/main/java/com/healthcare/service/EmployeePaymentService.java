package com.healthcare.service;

import com.healthcare.model.entity.EmployeePayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface EmployeePaymentService extends IService<EmployeePayment>, IFinder<EmployeePayment> {
    EmployeePayment save(EmployeePayment employee);

    Page<EmployeePayment> findPaymentByEmployee(Long employeeId, Date startDate, Date endDate, Pageable pageable);

    Page<EmployeePayment> findPaymentByAgency(Long agencyId, Date startDate, Date endDate, Pageable pageable);

    Page<EmployeePayment> findPaymentByCompany(Long companyId, Date startDate, Date endDate, Pageable pageable);

    Page<EmployeePayment> findAllPayments( Date startDate, Date endDate, Pageable pageable);

    Page<EmployeePayment> calculatePaymentByTimeRange(Long companyId, Long agencyId, Long employeeId, Date startDate, Date endDate, Pageable pageable);

    Page<EmployeePayment> calculatePaymentByTimeRangeEmployee(Long employeeId, Date startDate, Date endDate, Pageable pageable);

    Page<EmployeePayment> calculatePaymentByTimeRangeAgency(Long agencyId, Date startDate, Date endDate, Pageable pageable);

    Page<EmployeePayment> calculatePaymentByTimeRangeCompany(Long companyId, Date startDate, Date endDate, Pageable pageable);
}
