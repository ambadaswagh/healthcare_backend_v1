package com.healthcare.repository;

import java.util.Date;
import java.util.List;

import com.healthcare.model.entity.User;
import com.healthcare.model.enums.VisitStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healthcare.model.entity.Visit;
import com.healthcare.model.enums.StatusEnum;

import javax.transaction.Transactional;

public interface VisitRepository extends JpaRepository<Visit, Long>, JpaSpecificationExecutor<Visit>  {

    @Query(value = "select v from Visit v where v.pin = ?1 and DATE(v.checkInTime) = ?2")
    List<Visit> findByPin(String pin, Date date);

    @Query(value = "select v.* from visit v where v.pin = ?1 and v.check_in_time >= ?2 and v.check_in_time < ?3 and v.status = 'CHECK_IN' order by v.id limit 1",
            nativeQuery = true)
    Visit findByPinAndDate(String pin, Date startDate, Date endDate);
    
    @Query(value = "select v.* from visit v where v.pin = ?1 and v.check_in_time >= ?2 and v.check_in_time < ?3 and v.status = 'CHECK_IN' and v.agency_id=?4 order by v.id limit 1",
            nativeQuery = true)
    Visit findByPinDateAgency(String pin, Date startDate, Date endDate, Long agencyId);

    @Query(value = "select * from visit v where v.pin = ?1 and v.id = ?2", nativeQuery = true)
    Visit findByByBinAndId(String pin, long id);

    @Query(value = "select * from visit v where v.serviceplan_id = ?1", nativeQuery = true)
    List<Visit> findAllByServicePlanId(@Param("servicePlanId") long servicePlanId);

    @Query(value = "select * from visit v where v.agency_id = ?1", nativeQuery = true)
    List<Visit> findAllByAgencyId(@Param("agencyId") long agencyId);

    @Query(value = "select u.username userName, count(v.id) noOfVisit, CAST(AVG(TIMESTAMPDIFF(HOUR, v.check_in_time, v.check_out_time))  AS DECIMAL(12,2)) averageHour  from visit v left join user u on v.user_id = u.id where v.check_in_time >= ?1 and v.check_out_time <= ?2 group by v.user_id", nativeQuery = true)
    List<Object[]> findAllInPeriod(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Query(value = "select m.id , o.name organizationName, m.name mealName, m.meal_class mealClass, count(m.id) quantity, v.status, m.meal_status mealStatus from visit v, user u, organization o, meal m where v.user_id = u.id and m.restaurant_id= o.id and v.selected_lunch_id = m.id group by m.id", nativeQuery = true)
    List<Object[]> findAllVisitSummary();


    @Query(value = "select m.id , o.name organizationName, m.name mealName, m.meal_class mealClass, count(m.id) quantity, v.status, m.meal_status mealStatus  from visit v, user u, organization o, meal m " +
            "where v.user_id = u.id and m.restaurant_id= o.id and v.check_in_time >=?1 and v.check_in_time <?2 and (v.selected_lunch_id = m.id OR v.selected_dinner_id = m.id OR v.selected_breakfast_id = m.id) group by m.id, v.status", nativeQuery = true)
    List<Object[]> findAllVisitSummary(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "select m.id , o.name organizationName, m.name mealName, m.meal_class mealClass, count(m.id) quantity, v.status, m.meal_status mealStatus  from visit v, user u, organization o, meal m " +
            "where v.user_id = u.id and m.restaurant_id= o.id and v.check_in_time >=?1 and v.check_in_time <?2 and v.agency_id = ?3 and (v.selected_lunch_id = m.id OR v.selected_dinner_id = m.id OR v.selected_breakfast_id = m.id) group by m.id, v.status", nativeQuery = true)
    List<Object[]> findAllVisitSummaryByAgency(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("agencyId") Long agencyId);


    @Query(value = "select m.id , o.name organizationName, m.name mealName, m.meal_class mealClass, count(m.id) quantity, v.status, m.meal_status mealStatus  from visit v, user u, organization o, meal m " +
            "where v.user_id = u.id and m.restaurant_id= o.id and v.check_in_time >=?1 and v.check_in_time <?2 and v.agency_id IN ?3 and (v.selected_lunch_id = m.id OR v.selected_dinner_id = m.id OR v.selected_breakfast_id = m.id) group by m.id, v.status", nativeQuery = true)
    List<Object[]> findAllVisitSummaryByCompany(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("agencyIds") List<Long> agencyIds);


    @Query(value = "select v from Visit v where v.status in (:checkIn, :checkOut, :reserved)")
    Page<Visit> findAll(Pageable pageable,
                        @Param("checkIn") String checkIn,
                        @Param("checkOut") String checkOut,
                        @Param("reserved") String reserved);


    @Query(value = "select v from Visit v "
            + " where v.agency.company.id = :companyId "
            + " and (:agencyId is null or v.agency.id = :agencyId) "
            + " and (:startDate is null OR v.checkInTime >= :startDate) "
            + " and (:endDate is null OR v.checkInTime >= :endDate) "
            + " and (:userId is NULL OR v.user.id = :userId )")
    List<Visit> findVisitReport(@Param("companyId") Long companyId,
                                @Param("agencyId") Long agencyId,
                                @Param("startDate") Date startDate,
                                @Param("endDate") Date endDate,
                                @Param("userId") Long userId);

    @Query(value = "select v from Visit v "
            + " where v.agency.company.id = :companyId "
            + " and (:agencyId is null or v.agency.id = :agencyId) "
            + " and (:startDate is null OR v.checkInTime >= :startDate) "
            + " and (:endDate is null OR v.checkInTime >= :endDate) "
            + " and (:userId is NULL OR v.user.id = :userId )")
    Page<Visit> findVisitReport(@Param("companyId") Long companyId,
                                @Param("agencyId") Long agencyId,
                                @Param("startDate") Date startDate,
                                @Param("endDate") Date endDate,
                                @Param("userId") Long userId, Pageable pageable);


    @Query(value = "select v.* from visit v "
            + " inner join agency a on v.agency_id = a.id "
            + " inner join company c on a.company_id = c.id "
            + " inner join user u on v.user_id = u.id "
            + " where a.company_id = ?1 "
            + " and v.agency_id = ?2 "
            + " and ( "
            + "         ( DATE(v.check_in_time) >=?3 "
            + "             and DATE(v.check_out_time) <=?4"
            + "          )  "
            + "          OR  DATE(v.check_out_time) between ?3 and ?4 "
            + "      ) "
            + " and u.status in ?5 "
            , nativeQuery = true)
    List<Visit> findAllSeniorsVisit(@Param("companyId") Long companyId,
                                    @Param("agencyId") Long agencyId,
                                    @Param("startDate") Date startDate,
                                    @Param("endDate") Date endDate,
                                    @Param("statusIds") List<StatusEnum> statusIds);

    @Query(value = "select count(distinct v.id) from visit v "
            + " inner join agency a on v.agency_id = a.id "
            + " inner join company c on a.company_id = c.id "
            + " inner join user u on v.user_id = u.id "
            + " where a.company_id = ?1"
            + " and v.agency_id = ?2 "
            + " and ( "
            + "         ( DATE(v.check_in_time) >=?3 "
            + "             and DATE(v.check_out_time) <=?4"
            + "          )  "
            + "          OR  DATE(v.check_out_time) between ?3 and ?4 "
            + "      ) "
            + " and u.status in ?5 "
            , nativeQuery = true)
    Long findAllCompleteCheckedInOutVisitCount(@Param("companyId") Long companyId,
                                               @Param("agencyId") Long agencyId,
                                               @Param("startDate") Date startDate,
                                               @Param("endDate") Date endDate,
                                               @Param("statusIds") List<StatusEnum> statusIds);


    @Query(value = "select count(distinct v.id) from visit v "
            + " inner join agency a on v.agency_id = a.id "
            + " inner join company c on a.company_id = c.id "
            + " inner join user u on v.user_id = u.id "
            + " where a.company_id = ?1 "
            + " and v.agency_id = ?2 "
            + " and DATE(v.check_in_time) between ?3 and ?4 "
            + " and check_out_time is null "
            + " and u.status in ?5 "
            , nativeQuery = true)
    Long findAllOnlyCheckedInVisitCount(@Param("companyId") Long companyId,
                                        @Param("agencyId") Long agencyId,
                                        @Param("startDate") Date startDate,
                                        @Param("endDate") Date endDate,
                                        @Param("statusIds") List<StatusEnum> statusIds);


    @Query(value = "select v.* from visit v inner join user u on v.user_id = u.id " +
            "where DATE(v.check_in_time) >=:startDate AND DATE(v.check_in_time) <=:endDate ORDER BY v.check_in_time ASC ", nativeQuery = true)
    List<Visit> findAllVisitors(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    @Query(value = "SELECT v from Visit v " +
            "WHERE DATE(v.checkInTime) >=:startDate " +
            "AND DATE(v.checkInTime) <=:endDate " +
            "AND v.status in (:checkIn, :checkOut, :reserved) " +
            "AND (:isReservedFilter = false or v.reservedDate is not null) " +
            "AND (:isAbsentFilter = false or v.reservedDate is not null) " +
            "AND (:isComeWithoutAuthFilter = false or v.reservedDate is null) " +
            "AND (:isStayLessHoursFilter = false or (v.checkInTime is not null AND v.checkOutTime is not null AND TIMESTAMPDIFF(HOUR,v.checkInTime,v.checkOutTime) < :requiredHours)) " +
            "AND (:isSuperAdmin = true or v.agency.id in (:ids)) " +
            "ORDER BY v.checkInTime DESC")
    Page<Visit> findAllVisitorByTime(@Param("startDate") Date startDate,
                                     @Param("endDate") Date endDatem, Pageable pageable,
                                     @Param("checkIn") String checkIn,
                                     @Param("checkOut") String checkOut,
                                     @Param("reserved") String reserved,
                                     @Param("isReservedFilter") boolean isReservedFilter,
                                     @Param("isAbsentFilter") boolean isAbsentFilter,
                                     @Param("isComeWithoutAuthFilter") boolean isComeWithoutAuthFilter,
                                     @Param("isStayLessHoursFilter") boolean isStayLessHoursFilter,
                                     @Param("isSuperAdmin") boolean isSuperAdmin,
                                     @Param("ids") List<Long> ids,
                                     @Param("requiredHours") Long requiredHours);

    @Query(value = "SELECT v from Visit v where DATE(v.checkInTime) = :date ")
    List<Visit> getStatisticsForToday(@Param("date") Date date);

    @Query(value = "SELECT v from Visit v where v.agency.id IN :agencyIds AND DATE(v.checkInTime) = :date")
    List<Visit> getStatisticsForTodayByAgencyIds(@Param("date") Date date, @Param("agencyIds") List<Long> agencyIds);

    @Query(value = "SELECT v from Visit v where v.agency.id = :agencyId AND DATE(v.checkInTime) = :date ")
    List<Visit> getStatisticsForTodayByAgencyId(@Param("date") Date date, @Param("agencyId") Long agencyId);

    @Transactional
    Long deleteByUser(User user);

    @Modifying
    @Transactional
    @Query(" DELETE FROM Visit WHERE user = :user AND status = 'RESERVED'")
    void deleteVisitByUser(@Param("user") User user);

    @Query(value = "select v from Visit v where v.agency.id = ?1")
    Page<Visit> findAllByAgencyId(@Param("agencyId") long agencyId, Pageable pageable);

    @Query(value = "select v.* from visit v where DATE(v.check_in_time) >=:startDate AND DATE(v.check_in_time) <=:endDate AND v.agency_id in :agencyIds " +
                    "ORDER BY v.check_in_time ASC ", nativeQuery = true)
    List<Visit> findAllVisitorsByAgencies(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                          @Param("agencyIds") List<Long> agencyIds);

    @Query(value = "select v.* from visit v where DATE(v.check_in_time) >=:startDate AND DATE(v.check_in_time) <=:endDate AND v.user_id in :userIds " +
            "ORDER BY v.check_in_time ASC ", nativeQuery = true)
    List<Visit> findAllVisitorsBySeniors(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                          @Param("userIds") List<Long> userIds);

    @Query(value = "select v.* from visit v where DATE(v.check_in_time) >=:startDate AND DATE(v.check_in_time) <=:endDate AND v.agency_id in :agencyIds " +
            " AND v.user_id in :userIds ORDER BY v.check_in_time ASC ", nativeQuery = true)
    List<Visit> findAllVisitorsByAgenciesAndSeniors(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                                        @Param("agencyIds") List<Long> agencyIds, @Param("userIds") List<Long> userIds);

    @Query(value = "select v from Visit v where v.agency.id = ?1 AND v.user.id = ?2 " +
            "AND v.checkOutTime is null AND DATE(v.checkInTime) >= ?3 AND DATE(v.checkInTime) <= ?4")
    List<Visit> findByAgencyUser(Long agencyId, Long userId, Date startDate, Date endDate);

    @Query(value = "select v.* from visit v "
            + " where DATE(v.check_in_time) >=?1 and DATE(v.check_in_time) <=?2 "
            + " and v.check_out_time is null and v.status = 'CHECK_IN'"
            , nativeQuery = true)
    List<Visit> findAllOnlyCheckedInVisit(@Param("startDate") Date startDate,
                                          @Param("endDate") Date endDate);

    @Query(value = "select v.* from visit v "
            + " where v.agency_id = ?1 AND DATE(v.check_in_time) >= ?2 and DATE(v.check_in_time) <=?3 "
            , nativeQuery = true)
    List<Visit> findAllOnlyCheckedInVisit( @Param("agencyId") Long agencyId, @Param("startDate") Date startDate,
                                           @Param("endDate") Date endDate);

    @Query(value = "select v.* from visit v where v.agency_id = ?1 and v.check_out_time is null" +
            " and v.status = 'RESERVED' and v.reserved_date >= ?2 and v.reserved_date <= ?3", nativeQuery = true)
    List<Visit> findAllReservedByToday(@Param("agencyId") Long agencyId, @Param("startDate") Date startDate,
                                       @Param("endDate") Date endDate);

    @Query(value = "select v.* from visit v where v.agency_id = ?1 and v.check_out_time is null and v.pin = ?2" +
            " and v.status = ?5 and v.reserved_date >= ?3 and v.reserved_date < ?4 order by v.id limit 1", nativeQuery = true)
    Visit findOneByStatusByToday(@Param("agencyId") Long agencyId, @Param("pin") String pin, @Param("startDate") Date startDate,
                                 @Param("endDate") Date endDate, @Param("status") String status);

    @Query(value = "select u.first_name, u.last_name, m.name mealName, v.pin, m.meal_class from visit v, meal m, agency a, user u where v.check_in_time >=?1 and v.check_in_time <?2 " +
            "and (v.selected_lunch_id = m.id OR v.selected_dinner_id = m.id OR v.selected_breakfast_id = m.id) and m.agency_id = a.id and u.agency_id = a.id and v.user_id = u.id",
            nativeQuery = true)
    List<Object[]> findMealOrderReport(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "select u.first_name, u.last_name, m.name mealName, v.pin, m.meal_class from visit v, meal m, agency a, user u where v.check_in_time >=?1 and v.check_in_time <?2 and a.id IN ?3 " +
            "and (v.selected_lunch_id = m.id OR v.selected_dinner_id = m.id OR v.selected_breakfast_id = m.id) and m.agency_id = a.id and u.agency_id = a.id and v.user_id = u.id",
            nativeQuery = true)
    List<Object[]> findMealOrderReportByCompany(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("agencyIds") List<Long> agencyIds);

    @Query(value = "select u.first_name, u.last_name, m.name mealName, v.pin, m.meal_class from visit v, meal m, agency a, user u where v.check_in_time >=?1 and v.check_in_time <?2 and a.id = ?3 " +
            "and (v.selected_lunch_id = m.id OR v.selected_dinner_id = m.id OR v.selected_breakfast_id = m.id) and m.agency_id = a.id and u.agency_id = a.id and v.user_id = u.id",
            nativeQuery = true)
    List<Object[]> findMealOrderReportByAgency(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("agencyIds") Long agencyId);

}
