package com.healthcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.dto.DoctorAppointmentDTO;
import com.healthcare.model.entity.DoctorAppointment;

@Repository
public interface DoctorAppointmentRepository extends JpaRepository<DoctorAppointment, Integer> {


    @Query(value = " select * from doctor_appointment where (user_id = :#{#query.userId} or :#{#query.userId} is null or :#{#query.userId} = '')" 
    				+" and ((appointment_time >= STR_TO_DATE(:#{#query.fromDate}, '%Y-%m-%d') or :#{#query.fromDate} is null or :#{#query.fromDate} = '') and (appointment_time <= STR_TO_DATE(:#{#query.toDate}, '%Y-%m-%d') or :#{#query.toDate} is null or :#{#query.toDate} = ''))"
    				+" and (doctor_tel like CONCAT('%', :#{#query.doctorTel}, '%') or :#{#query.doctorTel} is null or :#{#query.doctorTel} = '')"
    				+" and (doctor_name like CONCAT('%', :#{#query.doctorName}, '%') or :#{#query.doctorName} is null or :#{#query.doctorName} = '')"
    				+" and (status = :#{#query.status} or :#{#query.status} is null or :#{#query.status} = '') order by appointment_time desc"
    				+" limit :#{#query.perpage} offset :#{#query.offset}"
            , nativeQuery = true)
    public List<DoctorAppointment> search(@Param("query") DoctorAppointmentDTO query);
}