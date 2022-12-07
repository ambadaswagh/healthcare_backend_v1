package com.healthcare.repository;

import com.healthcare.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {


    @Query("SELECT no FROM Notification no ORDER BY id DESC")
    public List<Notification> findNotifications();

    @Query(value = "SELECT no.* FROM notification no where no.agency_id = ?1 limit 1", nativeQuery = true)
    Notification findByAgencyId(Long agencyId);
}