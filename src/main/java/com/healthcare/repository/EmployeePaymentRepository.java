package com.healthcare.repository;

import com.healthcare.model.entity.EmployeePayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface EmployeePaymentRepository extends JpaRepository<EmployeePayment, Long>, JpaSpecificationExecutor<EmployeePayment> {
    @Query(value = "select * from employee_payment e where e.employee_id = ?1 order by id desc limit 1", nativeQuery = true)
    EmployeePayment getLastPaymentByEmployee(@Param("employeeId") Long employeeId);

    @Query(value = "select p from EmployeePayment p where p.employee.id = ?1 AND DATE(p.date) >= ?2 AND DATE(p.date) <= ?3")
    Page<EmployeePayment> findPaymentByEmployee(Long employeeId, Date startDate, Date endDate, Pageable pageable);

    @Query(value = "select p from EmployeePayment p where p.employee.id IN (select emp.id from Employee emp where emp.agency.id = ?1) AND DATE(p.date) >= ?2 AND DATE(p.date) <= ?3 ")
    Page<EmployeePayment> findPaymentByAgency(Long agencyId, Date startDate, Date endDate, Pageable pageable);

    @Query(value = "select p from EmployeePayment p where p.employee.id IN " +
            "(select emp.id from Employee emp where emp.agency.id IN (select agen.id from Agency agen where agen.company.id = ?1)) AND DATE(p.date) >= ?2 AND DATE(p.date) <= ?3 ")
    Page<EmployeePayment> findPaymentByCompany(Long companyId, Date startDate, Date endDate, Pageable pageable);

    @Query(value = "select p from EmployeePayment p where DATE(p.date) >= ?1 AND DATE(p.date) <= ?2")
    Page<EmployeePayment> findAllPayments( Date startDate, Date endDate, Pageable pageable);


    @Query(value = "select p from EmployeePayment p where p.employee.id = ?1 AND DATE(p.date) >= ?2 AND DATE(p.date) <= ?3")
    List<EmployeePayment> findPaymentByEmployeeList(Long employeeId, Date startDate, Date endDate);

    @Query(value = "select p from EmployeePayment p where p.employee.id IN (select emp.id from Employee emp where emp.agency.id = ?1) AND DATE(p.date) >= ?2 AND DATE(p.date) <= ?3 ")
    List<EmployeePayment> findPaymentByAgencyList(Long agencyId, Date startDate, Date endDate);

    @Query(value = "select p from EmployeePayment p where p.employee.id IN " +
            "(select emp.id from Employee emp where emp.agency.id IN (select agen.id from Agency agen where agen.company.id = ?1)) AND DATE(p.date) >= ?2 AND DATE(p.date) <= ?3 ")
    List<EmployeePayment> findPaymentByCompanyList(Long companyId, Date startDate, Date endDate);

    @Query(value = "select p from EmployeePayment p where DATE(p.date) >= ?1 AND DATE(p.date) <= ?2")
    List<EmployeePayment> findAllPaymentsList( Date startDate, Date endDate);

}
